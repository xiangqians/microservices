package org.microservices.common.auth.support;

import org.apache.commons.collections4.CollectionUtils;
import org.microservices.common.core.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link ResourceServerSecurityConfigurer}
 *
 * @author xiangqian
 * @date 18:25 2022/04/10
 */
@Component
public class EnhancedResourceServerSecurityConfigurer implements HttpSecurityConfigurer {

    private Set<RequestMatcher> requestMatchers;

    public EnhancedResourceServerSecurityConfigurer() {
        this.requestMatchers = new HashSet<>();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ResourceServerSecurityConfigurer resourceServerSecurityConfigurer = http.getConfigurer(ResourceServerSecurityConfigurer.class);
        resourceServerSecurityConfigurer.getAccessDeniedHandler();
        resourceServerSecurityConfigurer.tokenExtractor(tokenExtractor());
        resourceServerSecurityConfigurer.expressionHandler(OAuth2WebSecurityExpressionHandler());
    }

    @Autowired(required = false)
    public void setAnonymousAuthorizationRequests(List<AnonymousAuthorizationRequest> anonymousAuthorizationRequests) {
        addRequestMatchers(anonymousAuthorizationRequests);
    }

    @Autowired(required = false)
    public void setPermitAuthorizationRequests(List<PermitAuthorizationRequest> permitAuthorizationRequests) {
        addRequestMatchers(permitAuthorizationRequests);
    }

    private void addRequestMatchers(List<? extends AuthorizationRequest> authorizationRequests) {
        for (AuthorizationRequest authorizationRequest : authorizationRequests) {
            addRequestMatchers(authorizationRequest.get());
        }
    }

    private void addRequestMatchers(Set<ResourceMatchPattern> resourceMatchPatterns) {
        for (ResourceMatchPattern resourceMatchPattern : resourceMatchPatterns) {
            Set<String> patterns = resourceMatchPattern.getPatterns();
            if (CollectionUtils.isEmpty(patterns)) {
                continue;
            }

            String method = Optional.ofNullable(resourceMatchPattern.getMethod()).map(HttpMethod::toString).orElse(null);
            for (String pattern : patterns) {
                requestMatchers.add(new AntPathRequestMatcher(pattern, method));
            }
        }
    }

    private boolean matches(HttpServletRequest request) {
        for (RequestMatcher requestMatcher : requestMatchers) {
            if (requestMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 目的：在anonymous和permitAll开放指定url后不进行scope校验
     * <p>
     * 背景：配置access后，anonymous和permitAll匹配的url仍需要校验scope
     * <p>
     * 源码：
     * {@link OAuth2SecurityExpressionMethods#OAuth2SecurityExpressionMethods(org.springframework.security.core.Authentication)}
     * {@link OAuth2SecurityExpressionMethods#hasAnyScope(java.lang.String...)}
     * {@link OAuth2WebSecurityExpressionHandler#createEvaluationContextInternal(org.springframework.security.core.Authentication, org.springframework.security.web.FilterInvocation)}
     * {@link OAuth2WebSecurityExpressionHandler#OAuth2WebSecurityExpressionHandler()}
     * {@link ResourceServerSecurityConfigurer#expressionHandler}
     * {@link ResourceServerSecurityConfigurer#expressionHandler(SecurityExpressionHandler)}
     *
     * @return
     */
    private OAuth2WebSecurityExpressionHandler OAuth2WebSecurityExpressionHandler() {
        return new OAuth2WebSecurityExpressionHandler() {
            @Override
            protected StandardEvaluationContext createEvaluationContextInternal(Authentication authentication, FilterInvocation invocation) {
                StandardEvaluationContext ec = super.createEvaluationContextInternal(authentication, invocation);
                ec.setVariable("oauth2", new EnhancedOAuth2SecurityExpressionMethods(authentication));
                return ec;
            }
        };
    }


    public class EnhancedOAuth2SecurityExpressionMethods extends OAuth2SecurityExpressionMethods {
        public EnhancedOAuth2SecurityExpressionMethods(Authentication authentication) {
            super(authentication);
        }

        @Override
        public boolean hasAnyScope(String... scopes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            if (matches(request)) {
                return true;
            }
            return super.hasAnyScope(scopes);
        }
    }

    /**
     * 解决许可请求无效token问题！
     * 问题描述：当开放某个url请求后，如果此请求还携带token，那么将会出现"invalid_token"异常
     * <p>
     * 源码分析：
     * 1、
     * {@link OAuth2AuthenticationProcessingFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
     * 代码片段1：Authentication authentication = this.tokenExtractor.extract(request); 初步处理请求中token数据
     * 代码片段2：Authentication authResult = this.authenticationManager.authenticate(authentication); 如果authentication非空则校验token的有效性
     * <p>
     * 2、
     * 根据"1、"分析，要解决"许可请求无效token问题"需要重新设置 {@link OAuth2AuthenticationProcessingFilter#tokenExtractor}
     * 那么，如何设置？
     * <p>
     * 3、
     * 1）
     * {@link ResourceServerConfiguration#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)}
     * 创建 {@link ResourceServerSecurityConfigurer} 并apply到 {@link HttpSecurity}
     * 2）
     * {@link ResourceServerConfigurer#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)}
     * 先执行用户configure，
     * 再执行 {@link ResourceServerSecurityConfigurer#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)} （在此方法中将会创建 {@link OAuth2AuthenticationProcessingFilter} 实例）
     * <p>
     * 4、
     * 综上所述，
     * 从  {@link HttpSecurity} 获取到 {@link ResourceServerSecurityConfigurer} 实例，
     * 调 {@link ResourceServerSecurityConfigurer#tokenExtractor(TokenExtractor)} 方法设置 {@link TokenExtractor}
     *
     * @return
     */
    private TokenExtractor tokenExtractor() {
        return new BearerTokenExtractor() {
            @Override
            public Authentication extract(HttpServletRequest request) {
                if (matches(request)) {
                    return null;
                }
                return super.extract(request);
            }
        };
    }

}
