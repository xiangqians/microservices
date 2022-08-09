package org.microservices.common.register.support;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.cloud.consul.config.ConfigWatch;
import org.springframework.cloud.consul.config.ConsulConfigAutoConfiguration;
import org.springframework.cloud.consul.config.ConsulConfigIndexes;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * see {@link ConsulConfigAutoConfiguration}
 * <p>
 * {@link com.ecwid.consul.v1.kv.KeyValueConsulClient#getKVValues(java.lang.String, java.lang.String, com.ecwid.consul.v1.QueryParams)}
 * {@link org.springframework.cloud.consul.config.ConsulPropertySourceLocator#locate(org.springframework.core.env.Environment)}
 * {@link org.springframework.cloud.consul.config.ConsulPropertySource#parsePropertiesWithNonKeyValueFormat(java.util.List, org.springframework.cloud.consul.config.ConsulConfigProperties.Format)}
 * <p>
 * {@link ConfigWatch#watchConfigKeyValues()}
 * {@link org.springframework.cloud.consul.config.ConsulConfigAutoConfiguration.ConsulRefreshConfiguration#configWatch(org.springframework.cloud.consul.config.ConsulConfigProperties, org.springframework.cloud.consul.config.ConsulConfigIndexes, com.ecwid.consul.v1.ConsulClient, org.springframework.scheduling.TaskScheduler)}
 * {@link org.springframework.cloud.endpoint.event.RefreshEventListener}
 * {@link org.springframework.cloud.autoconfigure.RefreshAutoConfiguration#refreshEventListener(org.springframework.cloud.context.refresh.ContextRefresher)}
 *
 * @author xiangqian
 * @date 17:35 2022/04/30
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnConsulEnabled
@ConditionalOnProperty(name = {"spring.cloud.consul.config.enabled"}, matchIfMissing = true)
@EnableConfigurationProperties
public class ConsulConfigAutoConfigurationSupport implements ApplicationContextAware {

    public static final String CONFIG_WATCH_TASK_SCHEDULER_NAME = "configWatchTaskScheduler";

    public ConsulConfigAutoConfigurationSupport() {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({RefreshEndpoint.class})
    @ConditionalOnProperty(name = {"spring.cloud.consul.config.watch.enabled"}, matchIfMissing = true)
    public static class ConsulRefreshConfiguration {
        protected ConsulRefreshConfiguration() {
        }

        @Bean
        @ConditionalOnBean({ConsulConfigIndexes.class})
        public ConfigWatch configWatch(ApplicationContext applicationContext,
                                       ConsulConfigProperties properties,
                                       ConsulClient consul,
                                       ConsulConfigIndexes indexes,
                                       @Qualifier(CONFIG_WATCH_TASK_SCHEDULER_NAME) TaskScheduler taskScheduler) {
            return new ConfigWatchSupport(applicationContext, properties, consul, indexes.getIndexes(), taskScheduler);
        }

        @Bean(name = {CONFIG_WATCH_TASK_SCHEDULER_NAME})
        public TaskScheduler configWatchTaskScheduler() {
            return new ThreadPoolTaskScheduler();
        }
    }

    public static class ConfigWatchSupport extends ConfigWatch {
        public ConfigWatchSupport(ApplicationContext applicationContext,
                                  ConsulConfigProperties properties,
                                  ConsulClient consul,
                                  LinkedHashMap<String, Long> initialIndexes,
                                  TaskScheduler taskScheduler) {
            super(properties, new ConsulClientSupport(applicationContext, properties, consul), initialIndexes, taskScheduler);
        }
    }

    public static class ConsulClientSupport extends ConsulClient {

        private ConsulConfigProperties properties;
        private String active;
        private ConsulClient consulClient;
        private ConfigListener changeListener;

        public ConsulClientSupport(ApplicationContext applicationContext,
                                   ConsulConfigProperties properties,
                                   ConsulClient consulClient) {
            this.properties = properties;
            this.active = getSpringProfilesActive(applicationContext);
            this.consulClient = consulClient;
            this.changeListener = getConfigListenerBean(applicationContext);
        }

        @Override
        public Response<List<GetValue>> getKVValues(String keyPrefix, String token, QueryParams queryParams) {
            Response<List<GetValue>> response = consulClient.getKVValues(keyPrefix, token, queryParams);
            List<GetValue> values = null;
            if (changeListener != null && CollectionUtils.isNotEmpty(values = response.getValue())) {
                Iterator<GetValue> iterator = values.iterator();
                while (iterator.hasNext()) {
                    GetValue value = iterator.next();
                    String key = value.getKey();
                    if (keyPrefix.equals(key) || Objects.isNull(value.getValue())) {
                        continue;
                    }
                    Config config = new Config();
                    config.setName(key.indexOf(keyPrefix) == 0 ? key.substring(keyPrefix.length()) : key);
                    config.setContent(value.getDecodedValue(StandardCharsets.UTF_8));
                    if (!changeListener.changed(config)) {
                        iterator.remove();
                    }
                }
            }
            return response;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConfigListener configListener = getConfigListenerBean(applicationContext);
        if (Objects.isNull(configListener)) {
            return;
        }

        ConsulConfigProperties properties = applicationContext.getBean(ConsulConfigProperties.class);
        ConsulClient consul = applicationContext.getBean(ConsulClient.class);

        String prefix = properties.getPrefix();
        String defaultContext = properties.getDefaultContext();
        String profileSeparator = properties.getProfileSeparator();
        String active = getSpringProfilesActive(applicationContext);
//        String dataKey = properties.getDataKey();
        String keyPrefix = buildKeyPrefix(prefix, defaultContext, profileSeparator, active, null);
        String token = "";
        QueryParams queryParams = new QueryParams((String) null);
        Response<List<GetValue>> response = consul.getKVValues(keyPrefix, token, queryParams);
        List<GetValue> values = response.getValue();
        if (CollectionUtils.isNotEmpty(values)) {
            for (GetValue value : values) {
                String key = value.getKey();
                if (keyPrefix.equals(key) || Objects.isNull(value.getValue())) {
                    continue;
                }
                Config config = new Config();
                config.setName(key.indexOf(keyPrefix) == 0 ? key.substring(keyPrefix.length()) : key);
                config.setContent(value.getDecodedValue(StandardCharsets.UTF_8));
                configListener.init(config);
            }
        }
    }

    private static String buildKeyPrefix(String prefix, String defaultContext, String profileSeparator, String active, String dataKey) {
        String keyPrefix = String.format("%s/%s%s%s/%s", prefix, defaultContext, profileSeparator, active, StringUtils.trimToEmpty(dataKey));
        return keyPrefix;
    }

    private static String getSpringProfilesActive(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        return environment.getProperty("spring.profiles.active");
    }

    private static ConfigListener getConfigListenerBean(ApplicationContext applicationContext) {
        try {
            return applicationContext.getBean(ConfigListener.class);
        } catch (Exception e) {
            log.warn("{} bean不存在！", ConfigListener.class);
            return null;
        }
    }

}
