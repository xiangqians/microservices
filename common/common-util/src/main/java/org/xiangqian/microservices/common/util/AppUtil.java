package org.xiangqian.microservices.common.util;

import lombok.Getter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * 应用工具类
 *
 * @author xiangqian
 * @date 19:45 2023/07/07
 */
@Configuration(proxyBeanMethods = false)
public class AppUtil implements ApplicationContextAware {
    // 应用程序上下文
    private static ApplicationContext applicationContext;

    // 应用程序名称
    @Getter
    private static String name;

    @Getter
    private static boolean isWebMvc;

    @Getter
    private static boolean isWebFlux;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        initName();
        initSpringFramework();
    }

    private void initSpringFramework() {
        Map<String, ApplicationContextAware> applicationContextAwareMap = getBeansByType(ApplicationContextAware.class);
        if (MapUtils.isNotEmpty(applicationContextAwareMap)) {
            for (Map.Entry<String, ApplicationContextAware> entry : applicationContextAwareMap.entrySet()) {
                ApplicationContextAware applicationContextAware = entry.getValue();
                String className = applicationContextAware.getClass().getName();

                // 如果存在 org.springframework.web.servlet.DispatcherServlet Bean，表明使用的是 Spring Web 框架
                if ("org.springframework.web.servlet.DispatcherServlet" .equals(className)) {
                    isWebMvc = true;
                }

                // 如果存在 org.springframework.web.reactive.DispatcherHandler Bean，表明使用的是 Spring WebFlux 框架
                if ("org.springframework.web.reactive.DispatcherHandler" .equals(className)) {
                    isWebFlux = true;
                }
            }
        }
    }

    private void initName() {
        Environment environment = getBean(Environment.class);
        name = environment.getProperty("spring.application.name");
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> Map<String, T> getBeansByType(Class<T> requiredType) {
        return applicationContext.getBeansOfType(requiredType);
    }

}
