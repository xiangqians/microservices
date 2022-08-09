package org.microservices.common.monitor.support;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author xiangqian
 * @date 15:10:53 2022/04/05
 */
@Slf4j
public class MonitorMeterRegistryCustomizer implements MeterRegistryCustomizer<MeterRegistry> {

    @Override
    public void customize(MeterRegistry registry) {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            log.debug("设置metrics实例id为ip: {}", hostAddress);
            registry.config().commonTags("instance-id", hostAddress);
        } catch (Exception e) {
            String uuid = UUID.randomUUID().toString();
            registry.config().commonTags("instance-id", uuid);
            log.error(String.format("获取实例ip失败，设置实例id为uuid: %s", uuid), e);
        }
    }

}
