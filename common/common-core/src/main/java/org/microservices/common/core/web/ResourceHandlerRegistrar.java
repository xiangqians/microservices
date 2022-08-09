package org.microservices.common.core.web;


/**
 * 资源处理注册器
 * <p>
 * see {@link org.microservices.common.web.configure.WebMvcConfiguration#addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry)}
 *
 * @author xiangqian
 * @date 21:30 2022/06/20
 */
public interface ResourceHandlerRegistrar {

    void addResourceHandlers(ResourceHandlerRegistry registry);

    public static interface ResourceHandlerRegistry {
        ResourceHandlerRegistration addResourceHandler(String... pathPatterns);
    }

    public static interface ResourceHandlerRegistration {
        ResourceHandlerRegistration addResourceLocations(String... locations);
    }

}
