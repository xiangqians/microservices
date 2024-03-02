package org.xiangqian.microservices.common.register;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.consul.config.ConfigWatch;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * 获取KV源码:
 * {@link com.ecwid.consul.v1.kv.KeyValueConsulClient#getKVValues(java.lang.String, java.lang.String, com.ecwid.consul.v1.QueryParams)}
 * {@link org.springframework.cloud.consul.config.ConsulPropertySource#init()}
 * {@link org.springframework.cloud.consul.config.ConsulPropertySources#create(java.lang.String, com.ecwid.consul.v1.ConsulClient, java.util.function.BiConsumer)}
 * {@link org.springframework.cloud.consul.config.ConsulPropertySourceLocator#locate(org.springframework.core.env.Environment)}
 * {@link org.springframework.cloud.consul.config.ConsulPropertySource#parsePropertiesWithNonKeyValueFormat(java.util.List, org.springframework.cloud.consul.config.ConsulConfigProperties.Format)}
 * <p>
 * KV Watch源码:
 * {@link org.springframework.cloud.consul.config.ConfigWatch#watchConfigKeyValues()}
 * {@link org.springframework.cloud.consul.config.ConfigWatch#start()}
 * {@link org.springframework.cloud.consul.config.ConsulConfigAutoConfiguration.ConsulRefreshConfiguration#configWatch(org.springframework.cloud.consul.config.ConsulConfigProperties, org.springframework.cloud.consul.config.ConsulConfigIndexes, com.ecwid.consul.v1.ConsulClient, org.springframework.scheduling.TaskScheduler)}
 * {@link org.springframework.cloud.endpoint.event.RefreshEventListener}
 * {@link org.springframework.cloud.autoconfigure.RefreshAutoConfiguration#refreshEventListener(org.springframework.cloud.context.refresh.ContextRefresher)}
 *
 * @author xiangqian
 * @date 21:35 2022/04/30
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RegisterAutoConfiguration implements ApplicationListener<RefreshEvent> {

    @Override
    public void onApplicationEvent(RefreshEvent event) {
        ConfigWatch.RefreshEventData refreshEventData = (ConfigWatch.RefreshEventData) event.getEvent();
        log.debug("配置文件发生改变：{}", refreshEventData);
    }

}