package org.xiangqian.microservices.common.auth.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author xiangqian
 * @date 19:37 2023/09/07
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(prePostEnabled = true, // 开启预处理验证
        jsr250Enabled = true, // 启用JSR250注解支持
        securedEnabled = true) // 启用 {@link org.springframework.security.access.annotation.Secured} 注解支持
public class AuthAutoConfiguration {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // http授权请求配置
                .authorizeHttpRequests((authorize) -> authorize
                        // 放行静态资源
                        .requestMatchers("/favicon.ico", "/assets/**", "/webjars/**", "/login").permitAll()
                        // 放行doc资源
                        .requestMatchers("/v3/**", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/doc.html").permitAll()
                        // 其他请求需要授权
                        .anyRequest().authenticated())
                // Form login handles the redirect to the login page from the authorization server filter chain
//                .formLogin(Customizer.withDefaults())
                .formLogin(formLogin -> formLogin.loginPage("/login")) // 自定义登陆页
                // 添加BearerTokenAuthenticationFilter，将服务当做一个资源服务，解析请求头中的token
                .oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }

}
