package org.microservices.monitor.configure;

import org.microservices.common.auth.support.PermitAuthorizationRequest;
import org.microservices.common.auth.support.ResourceMatchPattern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @author xiangqian
 * @date 22:59 2022/07/19
 */
@Configuration
public class MonitorConfiguration {

    @Bean
    public PermitAuthorizationRequest monitorPermitAuthorizationRequest() {
        return () -> Set.of(new ResourceMatchPattern("/**"));
    }

}
