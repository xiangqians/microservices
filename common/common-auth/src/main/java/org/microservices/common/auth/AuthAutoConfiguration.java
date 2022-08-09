package org.microservices.common.auth;

import org.microservices.common.auth.support.*;
import org.microservices.common.core.annotation.Conditional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * ResourceServerConfiguration资源服务配置
 *
 * @author xiangqian
 * @date 09:04 2022/03/26
 */
@Configuration
@ComponentScan(basePackages = {"org.microservices.common.auth.configure"})
@Conditional(processor = ResourceServerConditionProcessor.class)
public class AuthAutoConfiguration {

    @Bean
    public HttpSecurityConfigurer authorizationRequestConfigurer() {
        return new AuthorizationRequestConfigurer();
    }

    @Bean
    public HttpSecurityConfigurer enhancedResourceServerSecurityConfigurer() {
        return new EnhancedResourceServerSecurityConfigurer();
    }

    @Bean
    public HttpSecurityConfigurer headerHttpSecurityConfigurer() {
        return new HeaderHttpSecurityConfigurer();
    }

    @Bean
    public HttpSecurityConfigurer accessHttpSecurityConfigurer() {
        return new AccessHttpSecurityConfigurer();
    }

    @Bean
    public AuthorizationRequest allowUnauthorizedRequestSupport() {
        return new AllowUnauthorizedRequestSupport();
    }

}
