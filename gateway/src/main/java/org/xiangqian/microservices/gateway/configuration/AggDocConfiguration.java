package org.xiangqian.microservices.gateway.configuration;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteRefreshListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.xiangqian.microservices.common.util.AppUtil;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 聚合子服务API文档
 *
 * @author xiangqian
 * @date 20:36 2022/04/02
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = {"springdoc.api-docs.enabled"}, matchIfMissing = true) // 开启openapi文档条件判断
public class AggDocConfiguration implements ApplicationListener<RefreshRoutesEvent>, ApplicationRunner, Runnable {

    @Value("${spring.cloud.gateway.discovery.locator.route-id-prefix}")
    private String routeIdPrefix;

    private SwaggerUiConfigProperties swaggerUiConfigProperties;
    private RouteDefinitionLocator routeDefinitionLocator;

    public AggDocConfiguration(SwaggerUiConfigProperties swaggerUiConfigProperties, RouteDefinitionLocator routeDefinitionLocator) {
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    @Override
    public void run() {
        List<RouteDefinition> routeDefinitions = routeDefinitionLocator.getRouteDefinitions().collectList().block();
        if (CollectionUtils.isEmpty(routeDefinitions)) {
            return;
        }

        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new LinkedHashSet<>(routeDefinitions.size(), 1f);
        for (RouteDefinition routeDefinition : routeDefinitions) {
            String routeId = routeDefinition.getId();
            if (!StringUtils.startsWith(routeId, routeIdPrefix)) {
                continue;
            }

            routeId = routeId.substring(routeIdPrefix.length());
            if (StringUtils.equalsAny(routeId, AppUtil.getName(), "consul", "monitor", "auth")) {
                continue;
            }

            String group = routeId;
            URI uri = routeDefinition.getUri(); // lb://user
            String url = String.format("%s/v3/api-docs", uri.toString().substring("lb://".length()));
            String displayName = group;
            AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl(group, url, displayName);
            urls.add(swaggerUrl);
        }
        swaggerUiConfigProperties.setUrls(urls);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        run();
    }

    // 多线程并发，以后再优化
    @Override
    public void onApplicationEvent(RefreshRoutesEvent event) {
        Object source = event.getSource();
        if (source instanceof RouteRefreshListener) {
            new Thread(this).start();
        }
    }

}

