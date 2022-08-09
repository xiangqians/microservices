package org.microservices.gateway.service.impl;

import org.springframework.context.ApplicationEvent;

/**
 * 路由事件
 *
 * @author xiangqian
 * @date 13:13:32 2022/04/06
 */
public class RouteEvent extends ApplicationEvent {

    public static RouteEvent ADD = new RouteEvent("ADD RouteEvent");
    public static RouteEvent DELETE = new RouteEvent("DELETE RouteEvent");
    public static RouteEvent UPDATE = new RouteEvent("UPDATE RouteEvent");

    private RouteEvent(Object source) {
        super(source);
    }

}
