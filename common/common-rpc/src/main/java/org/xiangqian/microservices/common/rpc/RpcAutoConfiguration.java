package org.xiangqian.microservices.common.rpc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Configuration;
import org.xiangqian.microservices.common.util.NamingConvUtil;
import org.xiangqian.microservices.common.util.ResourceUtil;

import java.util.Optional;
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

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registryApiFactoryBean(registry);
    }

    // 注册ApiFactoryBean
    @SneakyThrows
    public void registryApiFactoryBean(BeanDefinitionRegistry registry) throws BeansException {
        Set<Class<?>> classes = ResourceUtil.scanClasses("org.xiangqian.microservices.**.api");
        if (CollectionUtils.isEmpty(classes)) {
            return;
        }

        for (Class<?> clazz : classes) {
            // 服务名称 --> 约定大于配置
            String packageName = clazz.getPackageName();
            String serviceName = String.format("%s-biz", packageName.substring("org.xiangqian.microservices.".length(), packageName.length() - ".api".length()).replace(".", "-"));

            // Bean名称
            String beanName = String.format("%sFactoryBean", NamingConvUtil.upperCamelToLowerCamel(clazz.getSimpleName()));

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
