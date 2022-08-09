package org.microservices.monitor.support;

import de.codecentric.boot.admin.server.cloud.discovery.InstanceDiscoveryListener;
import de.codecentric.boot.admin.server.cloud.discovery.ServiceInstanceConverter;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 如果微服务模块没有依赖 'common-monitor' 模块，那么则不监控此模块
 * {@link reactor.core.publisher.FluxFilterFuseable.FilterFuseableSubscriber#onNext(java.lang.Object)}
 * {@link  de.codecentric.boot.admin.server.cloud.discovery.InstanceDiscoveryListener#shouldRegisterInstanceBasedOnMetadata(org.springframework.cloud.client.ServiceInstance)}
 * {@link de.codecentric.boot.admin.server.cloud.config.AdminServerDiscoveryAutoConfiguration#instanceDiscoveryListener(de.codecentric.boot.admin.server.cloud.discovery.ServiceInstanceConverter, org.springframework.cloud.client.discovery.DiscoveryClient, de.codecentric.boot.admin.server.services.InstanceRegistry, de.codecentric.boot.admin.server.domain.entities.InstanceRepository)}
 *
 * @author xiangqian
 * @date 23:14 2022/04/05
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "spring.boot.admin.discovery")
public class MonitorInstanceDiscoveryListener extends InstanceDiscoveryListener {


    public MonitorInstanceDiscoveryListener(ServiceInstanceConverter serviceInstanceConverter,
                                            DiscoveryClient discoveryClient,
                                            InstanceRegistry registry,
                                            InstanceRepository repository) {
        super(discoveryClient, registry, repository);
        setConverter(serviceInstanceConverter);
    }

    @Override
    protected boolean shouldRegisterInstanceBasedOnMetadata(ServiceInstance instance) {
        String managementPath = instance.getMetadata().get("management.endpoints.web.base-path");
        if (Objects.isNull(managementPath)) {
            log.warn("'{}' 微服务模块没有依赖 'common-monitor' 模块，则不监控此模块", instance.getServiceId());
            return false;
        }
        return super.shouldRegisterInstanceBasedOnMetadata(instance);
    }

}
