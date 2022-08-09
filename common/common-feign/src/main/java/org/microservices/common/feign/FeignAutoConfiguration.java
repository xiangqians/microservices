package org.microservices.common.feign;


import feign.Contract;
import feign.Logger;
import feign.RequestInterceptor;
import org.microservices.common.core.annotation.ConditionalOnBootAnnotation;
import org.microservices.common.core.annotation.YamlSource;
import org.microservices.common.feign.annotation.EnableFeign;
import org.microservices.common.feign.support.EnhancedOAuth2FeignRequestInterceptor;
import org.microservices.common.feign.support.FeignContract;
import org.springframework.cloud.commons.security.AccessTokenContextRelay;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import java.util.List;
import java.util.Objects;

/**
 * @author xiangqian
 * @date 17:20 2022/03/26
 */
@Configuration
@YamlSource({"classpath:common-feign.yml"})
@ConditionalOnBootAnnotation(value = EnableFeign.class)
@ComponentScan(basePackages = "org.microservices.**.fallback.service") // 扫描服务降级组件
public class FeignAutoConfiguration {

    @Bean
    public Contract feignContract(FeignClientProperties feignClientProperties,
                                  List<AnnotatedParameterProcessor> parameterProcessors,
                                  ConversionService feignConversionService) throws NoSuchFieldException, NoSuchMethodException {
        boolean decodeSlash = Objects.isNull(feignClientProperties) || feignClientProperties.isDecodeSlash();
        return new FeignContract(parameterProcessors, feignConversionService, decodeSlash);
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
                                                            OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails,
                                                            AccessTokenContextRelay accessTokenContextRelay) {
        return new EnhancedOAuth2FeignRequestInterceptor(oAuth2ClientContext,
                oAuth2ProtectedResourceDetails,
                accessTokenContextRelay);
    }

    @Bean
    public AccessTokenContextRelay accessTokenContextRelay(OAuth2ClientContext oAuth2ClientContext) {
        return new AccessTokenContextRelay(oAuth2ClientContext);
    }

    @Bean
    public OAuth2ClientContext oAuth2ClientContext() {
        return new DefaultOAuth2ClientContext();
    }

    @Bean
    public OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails() {
        return new AuthorizationCodeResourceDetails();
    }

    /**
     * 配置Feign日志
     * <p>
     * 日志的四个参数说明：
     * 1、NONE：默认的，不显示任何日志
     * 2、BASIC：仅记录请求方法、URL、响应状态码及执行时间
     * 3、HEADERS：除了BASIC中定义的信息之外，还有请求和响应的头信息
     * 4、FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据
     *
     * @return
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
