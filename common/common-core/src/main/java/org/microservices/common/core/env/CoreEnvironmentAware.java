package org.microservices.common.core.env;

import lombok.Getter;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @author xiangqian
 * @date 16:16 2022/04/06
 */
@Getter
public class CoreEnvironmentAware implements EnvironmentAware {

    private Environment environment;
    private String version;
    private String feignBasePath;
    private String monitorBasePath;

    @Override
    public final void setEnvironment(Environment environment) {
        this.environment = environment;
        this.version = environment.getProperty("microservices.application.version");
        this.feignBasePath = environment.getProperty("microservices.core.feign.base-path");
        this.monitorBasePath = environment.getProperty("microservices.core.monitor.base-path");
    }

}
