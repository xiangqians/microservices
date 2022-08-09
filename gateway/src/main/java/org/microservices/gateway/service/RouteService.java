package org.microservices.gateway.service;

import org.microservices.common.register.support.ConfigListener;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;
import java.util.Map;

/**
 * 路由服务
 *
 * @author xiangqian
 * @date 10:26:14 2022/03/19
 */
public interface RouteService extends ConfigListener {

    /**
     * 路由定义列表
     *
     * @return
     */
    List<RouteDefinition> list();

    /**
     * 路由定义MAP
     * <{@link RouteDefinition#getId()}, {@link RouteDefinition}>
     *
     * @return
     */
    Map<String, RouteDefinition> map();

    /**
     * 根据 {@link RouteDefinition#getId()} 获取路由定义
     *
     * @param id {@link RouteDefinition#getId()}
     * @return
     */
    RouteDefinition get(String id);

    /**
     * 根据 {@link RouteDefinition#getId()} 删除路由定义
     *
     * @param ids {@link RouteDefinition#getId()}
     */
    void delete(String... ids);

    /**
     * 保存路由定义
     *
     * @param routeDefinitions
     */
    void save(RouteDefinition... routeDefinitions);

}
