package org.xiangqian.microservices.common.rpc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.xiangqian.microservices.common.util.NamingUtil;
import org.xiangqian.microservices.common.util.ResourceUtil;

import java.util.Set;

/**
 * RPC配置
 *
 * @author xiangqian
 * @date 20:27 2023/09/11
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RpcAutoConfiguration implements BeanDefinitionRegistryPostProcessor {

    // 注册ApiFactoryBean
    @SneakyThrows
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Set<Class<?>> classes = ResourceUtil.getClasses("org.xiangqian.microservices.**.api");
        if (CollectionUtils.isEmpty(classes)) {
            return;
        }

        for (Class<?> clazz : classes) {
            // 服务名称（约定大于配置）
            String packageName = clazz.getPackageName();
            String serviceName = packageName.substring("org.xiangqian.microservices.".length(), packageName.length() - ".api".length()).replace(".", "-");
            serviceName = String.format("%s-biz", serviceName);

            // Bean名称
            String beanName = String.format("%sFactoryBean", NamingUtil.UpperCamel.convToLowerCamel(clazz.getSimpleName()));

            // Bean定义构建器
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ApiFactoryBean.class);
            beanDefinitionBuilder.addConstructorArgValue(serviceName);
            beanDefinitionBuilder.addConstructorArgValue(clazz);

            // 注册Bean定义
            registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

}
