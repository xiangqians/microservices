package org.microservices.common.auth.support;

import org.microservices.common.auth.enumeration.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods;

/**
 * 访问 {@link HttpSecurity} 配置器
 *
 * @author xiangqian
 * @date 14:48 2022/04/10
 */
public class AccessHttpSecurityConfigurer implements HttpSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 指定不同请求方式访问资源所需要的权限，一般查询是read，其余是write。
//                .antMatchers(HttpMethod.GET, ALL_MATCH_PATTERN).access("#oauth2.hasScope('read')")
//                .antMatchers(HttpMethod.GET, ALL_MATCH_PATTERN).access("#oauth2.hasAnyScope('all', 'read')")
                .antMatchers(HttpMethod.GET).access(hasAnyScopeExpression(Scope.READ))
                .antMatchers(HttpMethod.HEAD).access(hasAnyScopeExpression(Scope.READ))
                .antMatchers(HttpMethod.POST).access(hasAnyScopeExpression(Scope.WRITE))
                .antMatchers(HttpMethod.PUT).access(hasAnyScopeExpression(Scope.WRITE))
                .antMatchers(HttpMethod.PATCH).access(hasAnyScopeExpression(Scope.WRITE))
                .antMatchers(HttpMethod.DELETE).access(hasAnyScopeExpression(Scope.WRITE))
                .antMatchers(HttpMethod.OPTIONS).access(hasAnyScopeExpression(Scope.WRITE))
                .antMatchers(HttpMethod.TRACE).access(hasAnyScopeExpression(Scope.WRITE));
    }

    /**
     * {@link OAuth2SecurityExpressionMethods#hasAnyScope(java.lang.String...)}
     *
     * @param scopes
     */
    protected String hasAnyScopeExpression(Scope... scopes) {
        StringBuilder builder = new StringBuilder();
        builder.append("#oauth2.hasAnyScope");
        builder.append("(");
        for (int i = 0, length = scopes.length; i < length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append("'").append(scopes[i].getValue()).append("'");
        }
        builder.append(")");
        return builder.toString();
    }

}
