package org.xiangqian.microservices.common.webflux;

import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.webflux.api.MultipleOpenApiWebFluxResource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * @author xiangqian
 * @date 20:14 2023/09/06
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = {"springdoc.api-docs.enabled"}, matchIfMissing = true) // 开启openapi文档条件判断
public class WebFluxDocAutoConfiguration {

    // @org.springdoc.webflux.core.configuration.MultipleOpenApiSupportConfiguration::start

    /**
     * 无法加载 {@link org.springdoc.webflux.core.configuration.MultipleOpenApiSupportConfiguration} 配置问题
     * {@link org.springdoc.webflux.core.configuration.MultipleOpenApiSupportConfiguration#multipleOpenApiResource(java.util.List, org.springframework.beans.factory.ObjectFactory, org.springdoc.core.service.AbstractRequestService, org.springdoc.core.service.GenericResponseService, org.springdoc.core.service.OperationService, org.springdoc.core.properties.SpringDocConfigProperties, org.springdoc.core.providers.SpringDocProviders, org.springdoc.core.customizers.SpringDocCustomizers)}
     * {@link org.springdoc.webflux.api.MultipleOpenApiWebFluxResource#afterPropertiesSet()}
     */

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = {"springdoc.use-management-port"}, havingValue = "false", matchIfMissing = true)
    @Lazy(false)
    MultipleOpenApiWebFluxResource multipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis, ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, SpringDocConfigProperties springDocConfigProperties, SpringDocProviders springDocProviders, SpringDocCustomizers springDocCustomizers) {
        return new MultipleOpenApiWebFluxResource(groupedOpenApis, defaultOpenAPIBuilder, requestBuilder, responseBuilder, operationParser, springDocConfigProperties, springDocProviders, springDocCustomizers);
    }

    // @org.springdoc.webflux.core.configuration.MultipleOpenApiSupportConfiguration::end

}
