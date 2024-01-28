package org.xiangqian.microservices.common.doc.webmvc;

import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.webmvc.api.MultipleOpenApiWebMvcResource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * customizer
 * https://github.com/springdoc/springdoc-openapi/tree/master/springdoc-openapi-common/src/main/java/org/springdoc/core/customizers
 * <p>
 * 如何自定义一个实体类参数呢？
 * {@link org.springdoc.core.customizers.PropertyCustomizer#customize(io.swagger.v3.oas.models.media.Schema, io.swagger.v3.core.converter.AnnotatedType)}
 * {@link org.springdoc.api.AbstractOpenApiResource#calculatePath(org.springframework.web.method.HandlerMethod, org.springdoc.core.fn.RouterOperation, java.util.Locale, io.swagger.v3.oas.models.OpenAPI)}
 * {@link org.springdoc.core.service.AbstractRequestService#build(org.springframework.web.method.HandlerMethod, org.springframework.web.bind.annotation.RequestMethod, io.swagger.v3.oas.models.Operation, org.springdoc.core.models.MethodAttributes, io.swagger.v3.oas.models.OpenAPI)}
 * {@link org.springdoc.core.service.AbstractRequestService#buildParams(org.springdoc.core.models.ParameterInfo, io.swagger.v3.oas.models.Components, org.springframework.web.bind.annotation.RequestMethod, com.fasterxml.jackson.annotation.JsonView, java.lang.String)}
 * {@link org.springdoc.core.service.AbstractRequestService#buildParam(org.springdoc.core.models.ParameterInfo, io.swagger.v3.oas.models.Components, com.fasterxml.jackson.annotation.JsonView)}
 * {@link org.springdoc.core.service.GenericParameterService#calculateSchema(io.swagger.v3.oas.models.Components, org.springdoc.core.models.ParameterInfo, org.springdoc.core.models.RequestBodyInfo, com.fasterxml.jackson.annotation.JsonView)}
 * {@link org.springdoc.core.utils.SpringDocAnnotationsUtils#extractSchema(io.swagger.v3.oas.models.Components, java.lang.reflect.Type, com.fasterxml.jackson.annotation.JsonView, java.lang.annotation.Annotation[])}
 * <p>
 * {@link io.swagger.v3.oas.models.Components#getSchemas()}
 * {@link org.springdoc.webmvc.api.MultipleOpenApiWebMvcResource#openapiJson(jakarta.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, java.util.Locale)}
 * {@link org.springdoc.webmvc.api.OpenApiResource#openapiJson(jakarta.servlet.http.HttpServletRequest, java.lang.String, java.util.Locale)}
 * {@link io.swagger.v3.core.filter.SpecFilter#removeBrokenReferenceDefinitions(io.swagger.v3.oas.models.OpenAPI)}
 * <p>
 * <p>
 * 自定义 {@link org.springdoc.core.properties.SwaggerUiConfigParameters}
 * <p>
 * GET /v3/api-docs/swagger-config
 * {@link org.springdoc.webmvc.ui.SwaggerConfigResource#openapiJson(jakarta.servlet.http.HttpServletRequest)}
 * {@link org.springdoc.core.properties.SwaggerUiConfigParameters#getConfigParameters()}
 * <p>
 * see {@link org.springdoc.webmvc.ui.SwaggerConfig#swaggerUiConfigParameters(org.springdoc.core.properties.SwaggerUiConfigProperties)}
 * see {@link org.springdoc.core.properties.SwaggerUiConfigParameters}
 * <p>
 * <p>
 * 修改Get请求参数ref
 * {@link org.springdoc.core.customizers.ParameterCustomizer#customize(io.swagger.v3.oas.models.parameters.Parameter, org.springframework.core.MethodParameter)}
 * {@link org.springdoc.webmvc.core.service.RequestService#customiseParameter(io.swagger.v3.oas.models.parameters.Parameter, org.springdoc.core.models.ParameterInfo, java.util.List)}
 * {@link org.springdoc.webmvc.core.service.RequestService#build(org.springframework.web.method.HandlerMethod, org.springframework.web.bind.annotation.RequestMethod, io.swagger.v3.oas.models.Operation, org.springdoc.core.models.MethodAttributes, io.swagger.v3.oas.models.OpenAPI)}
 * {@link org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration#requestBuilder(org.springdoc.core.service.GenericParameterService, org.springdoc.core.service.RequestBodyService, org.springdoc.core.service.OperationService, java.util.Optional, org.springdoc.core.discoverer.SpringDocParameterNameDiscoverer)}
 * 在注入 {@link org.springdoc.webmvc.core.service.RequestService} Bean 时添加了 {@link org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean} 注解（当 {@link org.springdoc.webmvc.core.service.RequestService} Bean不存在时初始化），
 * 所以，我们可以自定义 {@link org.springdoc.webmvc.core.service.RequestService} Bean
 * <p>
 * <p>
 * 修改POST请求参数ref
 * {@link org.springdoc.core.customizers.ParameterCustomizer#customize(io.swagger.v3.oas.models.parameters.Parameter, org.springframework.core.MethodParameter)}
 * {@link org.springdoc.core.service.AbstractRequestService#build(org.springframework.web.method.HandlerMethod, org.springframework.web.bind.annotation.RequestMethod, io.swagger.v3.oas.models.Operation, org.springdoc.core.models.MethodAttributes, io.swagger.v3.oas.models.OpenAPI)}
 * {@link org.springdoc.core.service.RequestBodyService#calculateRequestBodyInfo(io.swagger.v3.oas.models.Components, org.springdoc.core.models.MethodAttributes, org.springdoc.core.models.ParameterInfo, org.springdoc.core.models.RequestBodyInfo)}
 * {@link org.springdoc.core.service.RequestBodyService#buildRequestBody(io.swagger.v3.oas.models.parameters.RequestBody, io.swagger.v3.oas.models.Components, org.springdoc.core.models.MethodAttributes, org.springdoc.core.models.ParameterInfo, org.springdoc.core.models.RequestBodyInfo)}
 * {@link org.springdoc.core.service.GenericParameterService#calculateSchema(io.swagger.v3.oas.models.Components, org.springdoc.core.models.ParameterInfo, org.springdoc.core.models.RequestBodyInfo, com.fasterxml.jackson.annotation.JsonView)}
 * {@link org.springdoc.core.service.RequestBodyService}
 * {@link org.springdoc.core.configuration.SpringDocConfiguration#requestBodyBuilder(org.springdoc.core.service.GenericParameterService)}
 * 在注入 {@link org.springdoc.core.service.RequestBodyService} Bean 时添加了 {@link org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean} 注解（当 {@link org.springdoc.core.service.RequestBodyService} Bean不存在时初始化），
 * 所以，我们可以自定义 {@link org.springdoc.core.service.RequestBodyService} Bean
 *
 * @date 19:23 2023/03/28
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = {"springdoc.api-docs.enabled"}, matchIfMissing = true) // 开启openapi文档条件判断
public class WebMvcDocAutoConfiguration {

    // @org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration::start

    /**
     * 多个模块依赖，加载 org.springframework.boot.autoconfigure.AutoConfiguration.imports 后，无法加载 {@link org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration} 配置问题
     * 加载 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 资源源码：
     * {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector}
     * {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector#getCandidateConfigurations(org.springframework.core.type.AnnotationMetadata, org.springframework.core.annotation.AnnotationAttributes)}
     * {@link org.springframework.boot.context.annotation.ImportCandidates#load(java.lang.Class, java.lang.ClassLoader)}
     * 无法加载 {@link org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration} 配置源码：
     * {@link org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration#multipleOpenApiResource(java.util.List, org.springframework.beans.factory.ObjectFactory, org.springdoc.core.service.AbstractRequestService, org.springdoc.core.service.GenericResponseService, org.springdoc.core.service.OperationService, org.springdoc.core.properties.SpringDocConfigProperties, org.springdoc.core.providers.SpringDocProviders, org.springdoc.core.customizers.SpringDocCustomizers)}
     * {@link org.springdoc.webmvc.api.MultipleOpenApiWebMvcResource#afterPropertiesSet()}
     * 最终发现是 @Conditional({MultipleOpenApiSupportCondition.class}) 条件判断不通过，导致 Configuration 没有被加载，从而 bean 没有初始化
     * {@link org.springframework.context.annotation.Condition#matches(org.springframework.context.annotation.ConditionContext, org.springframework.core.type.AnnotatedTypeMetadata)}
     * {@link org.springdoc.core.conditions.MultipleOpenApiSupportCondition#matches(org.springframework.context.annotation.ConditionContext, org.springframework.core.type.AnnotatedTypeMetadata)}
     * 如果匹配结果为true则加载。
     * 解决方式1：必须在启动类所在的项目生命并注入 {@link org.springdoc.core.models.GroupedOpenApi} Bean
     * 解决方式2：手动声明相关bean
     */

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = {"springdoc.use-management-port"}, havingValue = "false", matchIfMissing = true)
    @Lazy(false)
    MultipleOpenApiWebMvcResource multipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis, ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, SpringDocConfigProperties springDocConfigProperties, SpringDocProviders springDocProviders, SpringDocCustomizers springDocCustomizers) {
        return new MultipleOpenApiWebMvcResource(groupedOpenApis, defaultOpenAPIBuilder, requestBuilder, responseBuilder, operationParser, springDocConfigProperties, springDocProviders, springDocCustomizers);
    }

    // @org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration::end

}
