package org.microservices.monitor.support;

import de.codecentric.boot.admin.server.cloud.config.AdminServerDiscoveryAutoConfiguration;
import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 配置springboot admin请求endpoint基础地址
 * {@link DefaultServiceInstanceConverter#getManagementPath(org.springframework.cloud.client.ServiceInstance)}
 * {@link AdminServerDiscoveryAutoConfiguration#serviceInstanceConverter()}
 *
 * @author xiangqian
 * @date 22:05 2022/04/05
 */
@Component("serviceInstanceConverter")
public class MonitorServiceInstanceConverter extends DefaultServiceInstanceConverter {

    @Override
    protected String getManagementPath(ServiceInstance instance) {
        String managementPath = instance.getMetadata().get("management.endpoints.web.base-path");
        return StringUtils.hasText(managementPath) ? managementPath : "/actuator";
    }

}
