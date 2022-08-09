package org.microservices.common.monitor;

import org.microservices.common.auth.support.PermitAuthorizationRequest;
import org.microservices.common.auth.support.ResourceMatchPattern;
import org.microservices.common.core.annotation.YamlSource;
import org.microservices.common.monitor.env.MonitorEnvironmentAware;
import org.microservices.common.monitor.support.WebHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @author xiangqian
 * @date 21:50:56 2022/04/04
 */
@Configuration
@YamlSource({"classpath:microservices/monitor.yml"})
public class MonitorAutoConfiguration extends MonitorEnvironmentAware {

    @Bean
    public PermitAuthorizationRequest commonMonitorPermitAuthorizationRequest() {
        return () -> Set.of(new ResourceMatchPattern(String.format("%s/**", getEndpointsWebBasePath())));
    }

    @Bean
    public HealthIndicator webHealthIndicator() {
        return new WebHealthIndicator();
    }

//    @Bean
//    public MeterRegistryCustomizer monitorMeterRegistryCustomizer() {
//        return new MonitorMeterRegistryCustomizer();
//    }

}
