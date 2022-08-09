package org.microservices.common.feign.support;

import feign.Contract;
import feign.MethodMetadata;
import feign.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author xiangqian
 * @date 23:01 2022/03/26
 */
public class FeignContract extends SpringMvcContract implements Contract {

    @Value("${microservices.common.core.feign.base-path}")
    private String basePath;

    private Field decodeSlashField;
    private Method resolveMethod;

    public FeignContract(List<AnnotatedParameterProcessor> annotatedParameterProcessors, ConversionService conversionService, boolean decodeSlash) throws NoSuchMethodException, NoSuchFieldException {
        super(annotatedParameterProcessors, conversionService, decodeSlash);

        //
        decodeSlashField = SpringMvcContract.class.getDeclaredField("decodeSlash");
        decodeSlashField.setAccessible(true);
        resolveMethod = SpringMvcContract.class.getDeclaredMethod("resolve", String.class);
        resolveMethod.setAccessible(true);
    }

    @Override
    protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
        if (clz.getInterfaces().length == 0) {
            String uri = basePath;
            RequestMapping classAnnotation = AnnotatedElementUtils.findMergedAnnotation(clz, RequestMapping.class);
            if (classAnnotation != null && classAnnotation.value().length > 0) {
                String pathValue = Util.emptyToNull(classAnnotation.value()[0]);
                pathValue = resolve(pathValue);
                if (!pathValue.startsWith("/")) {
                    pathValue = "/" + pathValue;
                }
                uri += pathValue;
            }

            // set template uri
            data.template().uri(uri);
            boolean decodeSlash = getDecodeSlash();
            if (data.template().decodeSlash() != decodeSlash) {
                data.template().decodeSlash(decodeSlash);
            }
        }
    }

    private String resolve(String value) {
        try {
            return (String) resolveMethod.invoke(this, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean getDecodeSlash() {
        try {
            return decodeSlashField.getBoolean(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
// 前言：为了隐士的修改feign请求根路径
//
// ContractBeanPostProcessor
// feign读取uri源码
// feign.ReflectiveFeign.ParseHandlersByName.apply
// feign.Contract.BaseContract.parseAndValidateMetadata(java.lang.Class<?>)
// org.springframework.cloud.openfeign.support.SpringMvcContract.parseAndValidateMetadata
// feign.Contract.BaseContract.parseAndValidateMetadata(java.lang.Class<?>, java.lang.reflect.Method)
// org.springframework.cloud.openfeign.support.SpringMvcContract.processAnnotationOnClass
//
// 何时创建org.springframework.cloud.openfeign.support.SpringMvcContract实例？
//
/*
// org.springframework.cloud.openfeign.FeignClientsConfiguration
@Bean
// @ConditionalOnMissingBean，它是修饰bean的一个注解，主要实现的是，当你的bean被注册之后，如果而注册相同类型的bean，就不会成功，它会保证你的bean只有一个，即你的实例只有一个，当你注册多个相同的bean时，会出现异常，以此来告诉开发人员。
@ConditionalOnMissingBean
public Contract feignContract(ConversionService feignConversionService) {
    boolean decodeSlash = Objects.isNull(this.feignClientProperties) || this.feignClientProperties.isDecodeSlash();
    return new SpringMvcContract(this.parameterProcessors, feignConversionService, decodeSlash);
}
*/
// 如何覆盖上述的呢？
// 自定义BeanPostProcessor来覆盖
// 但是，自定义BeanPostProcessor无法拦截到Contract，原因如下：
/*
Contract使用AnnotationConfigApplicationContext来加载的，
而，自定义BeanPostProcessor却添加到AnnotationConfigServletWebServerApplicationContext中，导致无法拦截到Contract。
什么办？ -- 将自定义BeanPostProcessor添加到AnnotationConfigApplicationContext
// org.springframework.boot.ApplicationContextFactory
ApplicationContextFactory DEFAULT = (webApplicationType) -> {
    try {
        switch(webApplicationType) {
        case SERVLET:
            return new AnnotationConfigServletWebServerApplicationContext();
        case REACTIVE:
            return new AnnotationConfigReactiveWebServerApplicationContext();
        default:
            return new AnnotationConfigApplicationContext();
        }
    } catch (Exception var2) {
        throw new IllegalStateException("Unable create a default ApplicationContext instance, you may need a custom ApplicationContextFactory", var2);
    }
};
*/
//
// 如何将自定义BeanPostProcessor添加到AnnotationConfigApplicationContext？
// org.springframework.boot.SpringApplication.refresh
// org.springframework.context.support.AbstractApplicationContext.refresh
// org.springframework.context.support.AbstractApplicationContext.registerBeanPostProcessors
// org.springframework.context.support.PostProcessorRegistrationDelegate.registerBeanPostProcessors(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, org.springframework.context.support.AbstractApplicationContext)
//
// 发现AnnotationConfigServletWebServerApplicationContext的父ApplicationContext是AnnotationConfigApplicationContext
// 而我当前程序是能获取到AnnotationConfigServletWebServerApplicationContext，从而也能获取到AnnotationConfigApplicationContext，
// 那么如何通过代码的方式向AnnotationConfigApplicationContext添加一个BeanPostProcessor，如果能，问题解决。
// 行不通
//# Auto Configure
//#org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
//org.springframework.cloud.bootstrap.BootstrapConfiguration=\
//  org.microservices.common.feign.FeignConfiguration

/*
何处调用BeanPostProcessor相关方法？
// org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(java.lang.String, java.lang.Object, org.springframework.beans.factory.support.RootBeanDefinition)
protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
    if (System.getSecurityManager() != null) {
        AccessController.doPrivileged(() -> {
            this.invokeAwareMethods(beanName, bean);
            return null;
        }, this.getAccessControlContext());
    } else {
        this.invokeAwareMethods(beanName, bean);
    }

    Object wrappedBean = bean;
    if (Objects.isNull(mbd) || !mbd.isSynthetic()) {
        wrappedBean = this.applyBeanPostProcessorsBeforeInitialization(bean, beanName);
    }

    try {
        this.invokeInitMethods(beanName, wrappedBean, mbd);
    } catch (Throwable var6) {
        throw new BeanCreationException(mbd != null ? mbd.getResourceDescription() : null, beanName, "Invocation of init method failed", var6);
    }

    if (Objects.isNull(mbd) || !mbd.isSynthetic()) {
        wrappedBean = this.applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    return wrappedBean;
}
*/
