package org.xiangqian.microservices.common.webmvc.auth;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import org.xiangqian.microservices.common.util.JwkUtil;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源服务器（Resource Server）配置
 * 资源服务器（Resource Server）：托管受保护资源的服务器，用于验证令牌并提供资源给客户端。
 *
 * @author xiangqian
 * @date 19:37 2023/09/07
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(prePostEnabled = true, // 开启预处理验证
        jsr250Enabled = true, // 启用JSR250注解支持
        securedEnabled = true) // 启用 {@link org.springframework.security.access.annotation.Secured} 注解支持
public class WebMvcAuthAutoConfiguration {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> requestMappingInfoMap = requestMappingHandlerMapping.getHandlerMethods();
        if (MapUtils.isNotEmpty(requestMappingInfoMap)) {
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : requestMappingInfoMap.entrySet()) {
                RequestMappingInfo requestMappingInfo = entry.getKey();
                // 处理方法
                HandlerMethod handlerMethod = entry.getValue();
                Method method = handlerMethod.getMethod();

                // 判断方法是否有使用 RequestPermit 注解
                RequestPermit requestPermit = method.getAnnotation(RequestPermit.class);
                if (requestPermit != null) {

                    // 请求方法集
                    Set<RequestMethod> requestMethods = Optional.ofNullable(requestMappingInfo.getMethodsCondition()).map(RequestMethodsRequestCondition::getMethods).orElse(null);

                    // 请求路径
                    Set<PathPattern> pathPatterns = Optional.ofNullable(requestMappingInfo.getPathPatternsCondition()).map(PathPatternsRequestCondition::getPatterns).orElse(null);
                    Set<String> patterns = Optional.ofNullable(pathPatterns).map(pathPatterns0 -> pathPatterns0.stream().map(PathPattern::getPatternString).collect(Collectors.toSet())).orElse(null);

                    requestMatchers.add(new RequestMatcherImpl(requestMethods, patterns, requestPermit.inner()));
                }
            }
        }

        http
                // http授权请求配置
                .authorizeHttpRequests((authorize) -> authorize
                        // 放行资源
                        .requestMatchers(requestMatchers.toArray(RequestMatcher[]::new)).permitAll()
                        // 其他请求需要授权
                        .anyRequest().authenticated())
                // 添加BearerTokenAuthenticationFilter，将服务当做一个资源服务，解析请求头中的token
                .oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Data
    public static class RequestMatcherImpl implements RequestMatcher {
        private Set<String> requestMethods;
        private Set<String> patterns;
        private boolean inner;

        public RequestMatcherImpl(Set<RequestMethod> requestMethods, Set<String> patterns, boolean inner) {
            if (CollectionUtils.isNotEmpty(requestMethods)) {
                this.requestMethods = requestMethods.stream().map(Enum::name).collect(Collectors.toSet());
            }
            this.patterns = patterns;
            this.inner = inner;
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            if (CollectionUtils.isNotEmpty(requestMethods)) {
                if (!requestMethods.contains(request.getMethod())) {
                    return false;
                }
            }

            if (inner) {
                if (!"true".equals(request.getHeader("inner"))) {
                    return false;
                }
            }

            request.getRequestURI();

            return false;
        }
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        JWKSet jwkSet = new JWKSet(JwkUtil.genRsaKey());
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        Set<JWSAlgorithm> jwsAlgs = new HashSet();
        jwsAlgs.addAll(JWSAlgorithm.Family.RSA);
        jwsAlgs.addAll(JWSAlgorithm.Family.EC);
        jwsAlgs.addAll(JWSAlgorithm.Family.HMAC_SHA);
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor();
        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector(jwsAlgs, jwkSource);
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {
        });
        return new NimbusJwtDecoder(jwtProcessor);
    }

}
