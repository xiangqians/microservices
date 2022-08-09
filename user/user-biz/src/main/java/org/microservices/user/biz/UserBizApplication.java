package org.microservices.user.biz;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.microservices.common.auth.support.PermitAuthorizationRequest;
import org.microservices.common.auth.support.ResourceMatchPattern;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 09:36 2022/04/03
 */
@SpringBootApplication
public class UserBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserBizApplication.class, args);
    }

    @Bean
    public PermitAuthorizationRequest userPermitAuthorizationRequest() {
        return () -> Set.of(new ResourceMatchPattern("/**"));
    }

}

