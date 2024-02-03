package org.xiangqian.microservices.user.biz.configuration;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * @author xiangqian
 * @date 21:18 2023/09/05
 */
@Configuration(proxyBeanMethods = false)
public class UserConfiguration implements WebFluxConfigurer {

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

}
