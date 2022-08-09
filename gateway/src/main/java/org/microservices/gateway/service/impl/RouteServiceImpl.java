package org.microservices.gateway.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.microservices.common.core.util.JacksonUtils;
import org.microservices.common.register.support.Config;
import org.microservices.gateway.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 11:03:38 2022/03/19
 */
@Slf4j
@Service
public class RouteServiceImpl implements RouteService, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private RouteDefinitionRepository routeDefinitionRepository;

    @Override
    public List<RouteDefinition> list() {
        Flux<RouteDefinition> flux = routeDefinitionRepository.getRouteDefinitions();
        return flux.toStream().collect(Collectors.toList());
    }

    @Override
    public Map<String, RouteDefinition> map() {
        Flux<RouteDefinition> flux = routeDefinitionRepository.getRouteDefinitions();
        return flux.toStream().collect(Collectors.toMap(RouteDefinition::getId, routeDefinition -> routeDefinition));
    }

    @Override
    public RouteDefinition get(String id) {
        Map<String, RouteDefinition> routeDefinitionMap = map();
        return routeDefinitionMap.get(id);
    }

    @Override
    public void delete(String... ids) {
        if (ArrayUtils.isEmpty(ids)) {
            return;
        }

        // delete
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.setLength(0);
        Map<String, RouteDefinition> routeDefinitionMap = map();
        for (String id : ids) {
            RouteDefinition routeDefinition = routeDefinitionMap.get(id);
            if (routeDefinition != null) {
                routeDefinitionRepository.delete(Mono.just(id));
                messageBuilder.append('\n').append('\r').append("[-] ").append(routeDefinition);
            }
        }

        // publish event
        if (messageBuilder.length() > 0) {
            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            applicationEventPublisher.publishEvent(RouteEvent.DELETE);
            log.info("已发布删除路由事件:{}", messageBuilder);
            printRouteInfo();
        }
    }

    @Override
    public void save(RouteDefinition... routeDefinitions) {
        if (ArrayUtils.isEmpty(routeDefinitions)) {
            return;
        }

        // save
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.setLength(0);
        Map<String, RouteDefinition> routeDefinitionMap = map();
        for (RouteDefinition routeDefinition : routeDefinitions) {
            RouteDefinition routeDefinitionOld = routeDefinitionMap.get(routeDefinition.getId());
            // save
            if (Objects.isNull(routeDefinitionOld)) {
                routeDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
                messageBuilder.append('\n').append('\r').append("[ +] ").append(routeDefinition);
                continue;
            }

            // delete & save
            if (!routeDefinition.equals(routeDefinitionOld)) {
                routeDefinitionRepository.delete(Mono.just(routeDefinition.getId()));
                routeDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
                messageBuilder.append('\n').append('\r').append("[-+] ").append(routeDefinition);
                continue;
            }
        }

        // publish event
        if (messageBuilder.length() > 0) {
            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            applicationEventPublisher.publishEvent(RouteEvent.ADD);
            log.info("已发布新增路由事件:{}", messageBuilder);
            printRouteInfo();
        }
    }

    private void printRouteInfo() {
        StringBuilder messageBuilder = new StringBuilder();
        List<RouteDefinition> routeDefinitionList = list();
        for (RouteDefinition routeDefinition : routeDefinitionList) {
            messageBuilder.append('\n').append('\r').append(routeDefinition);
        }
        log.info("已配置的路由列表：{}", messageBuilder);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void init(Config config) {
        changed(config);
    }

    /**
     * 动态路由配置监听
     *
     * @param config
     */
    @Override
    public boolean changed(Config config) {
        if (!"dynamic-route.json".equals(config.getName())) {
            return true;
        }
        log.debug("[receive config] name={}, content=\n{}", config.getName(), config.getContent());
        try {
            List<RouteDefinition> routes = JacksonUtils.toObject(config.getContent(), new TypeReference<List<RouteDefinition>>() {
            });
            RouteDefinition[] routeDefinitions = routes.toArray(RouteDefinition[]::new);
            save(routeDefinitions);
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

}
