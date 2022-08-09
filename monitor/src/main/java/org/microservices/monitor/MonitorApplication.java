package org.microservices.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.web.InstancesController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 扩展Event Journal，后续再看看
 * {@link InstancesController#events()}
 * {@link InstanceEvent}
 * <p>
 * 显示：
 * 调用量？
 * 异常次数？
 * 熔断次数？
 * 降级次数？
 * <p>
 * 将项目打包成tar.gz，并配置sh启动文件
 *
 * @author xiangqian
 * @date 20:59 2022/04/04
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class MonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }

}
