package org.xiangqian.microservices.monitor.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author xiangqian
 * @date 21:05 2024/02/04
 */
@EnableWebFluxSecurity
@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration {

    private String contextPath;

    @Autowired
    public void setContextPath(Environment environment) {
        contextPath = environment.getProperty("spring.boot.admin.context-path");
        if (contextPath == null || "/".equals(contextPath)) {
            contextPath = "";
        }
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                // 放行静态资源
                .pathMatchers(String.format("%s/assets/**", contextPath)).permitAll()
                // 放行接口
                .pathMatchers(String.format("%s/login", contextPath)).permitAll()
                // 其他请求需要授权
                .anyExchange().authenticated()
                .and()
                // 使用表单登录
                .formLogin(formLogin ->
                        // 自定义登陆页
                        formLogin.loginPage(String.format("%s/login", contextPath)))
                // 自定义登出接口
                .logout(logout -> logout.logoutUrl(String.format("%s/logout", contextPath)))
                // 禁用 HTTP 基本认证（HTTP Basic Authentication）
                .httpBasic().disable()
                // 禁用 CSRF（跨站请求伪造）保护
                .csrf().disable()
                .build();
    }

}
