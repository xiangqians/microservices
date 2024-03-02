package org.xiangqian.microservices.common.monitor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.xiangqian.microservices.common.util.AppUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author xiangqian
 * @date 21:07 2024/02/02
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MonitorAutoConfiguration {

    @Bean
    public InfoContributor infoContributor(Environment environment) {
        return builder -> builder
                .withDetail("name", environment.getProperty("spring.application.name"))
                .withDetail("version", environment.getProperty("spring.application.version"))
                .withDetail("description", environment.getProperty("spring.application.description"))
                .withDetail("active", environment.getProperty("spring.profiles.active"))
                .withDetail("springframework", ((Supplier<String>) () -> {
                    List<String> list = new ArrayList<>(2);
                    if (AppUtil.isWebMvc()) {
                        list.add("WebMvc");
                    }
                    if (AppUtil.isWebFlux()) {
                        list.add("WebFlux");
                    }
                    return StringUtils.join(list, ", ");
                }).get());
    }

}
