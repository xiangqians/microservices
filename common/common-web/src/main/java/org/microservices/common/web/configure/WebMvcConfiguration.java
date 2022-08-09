package org.microservices.common.web.configure;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.microservices.common.core.enumeration.Enum;
import org.microservices.common.core.web.ResourceHandlerRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author xiangqian
 * @date 19:14 2022/06/19
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired(required = false)
    private List<ResourceHandlerRegistrar> resourceHandlerRegistrars;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (CollectionUtils.isEmpty(resourceHandlerRegistrars)) {
            return;
        }

        ResourceHandlerRegistrar.ResourceHandlerRegistry resourceHandlerRegistry = pathPatterns ->
                new ResourceHandlerRegistrar.ResourceHandlerRegistration() {
                    ResourceHandlerRegistration resourceHandlerRegistration;

                    @Override
                    public ResourceHandlerRegistrar.ResourceHandlerRegistration addResourceLocations(String... locations) {
                        resourceHandlerRegistration = registry.addResourceHandler(pathPatterns);
                        resourceHandlerRegistration.addResourceLocations(locations);
                        return this;
                    }
                };
        resourceHandlerRegistrars.forEach(resourceHandlerRegistrar -> resourceHandlerRegistrar.addResourceHandlers(resourceHandlerRegistry));
    }

    /**
     * Spring Boot HTTP接口（GET & POST）反序列枚举值配置
     * <p>
     * {@link org.springframework.core.convert.support.StringToEnumConverterFactory}
     * {@link DefaultConversionService#addScalarConverters(org.springframework.core.convert.converter.ConverterRegistry)}
     * {@link ConverterRegistry#addConverterFactory(org.springframework.core.convert.converter.ConverterFactory)}
     * {@link GenericConversionService.Converters#add(org.springframework.core.convert.converter.GenericConverter)}
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new ConverterFactory<String, Enum>() {
            @Override
            public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
                return source -> {
                    String value = StringUtils.trim(source);
                    if (StringUtils.isEmpty(value)) {
                        return null;
                    }

                    if (targetType.getPackageName().startsWith("org.microservices")) {
                        return (T) Enum.valueOf(targetType, value);
                    }

                    Assert.isTrue(targetType.isEnum(), String.format("%s 该类型不是一个枚举类型", targetType));
                    java.lang.Enum e = java.lang.Enum.valueOf((Class<? extends java.lang.Enum>) targetType, value);
                    return (T) e;
                };
            }
        });
        WebMvcConfigurer.super.addFormatters(registry);
    }

}
