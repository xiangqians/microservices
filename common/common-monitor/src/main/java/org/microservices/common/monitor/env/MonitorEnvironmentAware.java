package org.microservices.common.monitor.env;

import lombok.Getter;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @author xiangqian
 * @date 23:12 2022/07/06
 */
@Getter
public class MonitorEnvironmentAware implements EnvironmentAware {

    private Environment environment;
    private String endpointsWebBasePath;

    @Override
    public final void setEnvironment(Environment environment) {
        this.environment = environment;
        this.endpointsWebBasePath = environment.getProperty("management.endpoints.web.base-path");
    }

}
