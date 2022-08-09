package org.microservices.common.monitor.support;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

/**
 * 自定义Health Indicator
 *
 * @author xiangqian
 * @date 13:27:55 2022/04/05
 */
public class WebHealthIndicator extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.up().withDetail("timestamp", System.currentTimeMillis());
    }

}
