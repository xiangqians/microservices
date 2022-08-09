package org.microservices.common.web;

import org.microservices.common.web.configure.WebMvcConfiguration;
import org.microservices.common.web.handler.GlobalExceptionHandler;
import org.microservices.common.web.support.FeignServiceBeanDefinitionRegistryPostProcessor;
import org.microservices.common.web.support.WebMethodSecurityExpressionOperations;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiangqian
 * @date 21:49 2022/03/27
 */
@Configuration
@ImportAutoConfiguration({WebMvcConfiguration.class})
public class WebAutoConfiguration {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public BeanDefinitionRegistryPostProcessor customBeanDefinitionRegistryPostProcessor() {
        return new FeignServiceBeanDefinitionRegistryPostProcessor();
    }

    @Bean("pre")
    public WebMethodSecurityExpressionOperations webMethodSecurityExpressionOperations() {
        return new WebMethodSecurityExpressionOperations();
    }

}
