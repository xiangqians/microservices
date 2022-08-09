package org.microservices.gateway.support.doc;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * 在集成 Spring Cloud Gateway 网关的时候，会出现没有 basePath 的情况，例如定义的 /user、/order 等微服务前缀，因此我们需要在 Gateway 网关添加一个 Filter 过滤器
 * 注意点
 * 在集成Spring Cloud Gateway网关的时候,会出现没有basePath的情况(即定义的例如/user、/order等微服务的前缀),这个情况在使用zuul网关的时候不会出现此问题,因此,在Gateway网关需要添加一个Filter实体Bean,代码如下：
 *
 * @author xiangqian
 * @date 17:36:52 2022/04/02
 */
@Component
public class DocGatewayFilterFactory extends AbstractGatewayFilterFactory {
    private static final String HEADER_NAME = "X-Forwarded-Prefix";

    private static final String URI = "/v2/api-docs";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            if (!StringUtils.endsWithIgnoreCase(path, URI)) {
                return chain.filter(exchange);
            }
            String basePath = path.substring(0, path.lastIndexOf(URI));
            ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, basePath).build();
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);
        };
    }

}
//然后在配置文件指定这个filter
//
//spring:
//  application:
//    name: service-doc
//  cloud:
//    gateway:
//      discovery:
//        locator:
//          #          enabled: true
//          lowerCaseServiceId: true
//      routes:
//        - id: service-user
//          uri: lb://service-user
//          predicates:
//            - Path=/user/**
//          #            - Header=Cookie,Set-Cookie
//          filters:
//            - SwaggerHeaderFilter
//            - StripPrefix=1
//        - id:  service-order
//          uri: lb://service-order
//          predicates:
//            - Path=/order/**
//          filters:
//            - SwaggerHeaderFilter  //指定filter
//            - StripPrefix=1
//特别注意：如果是高版本的Spring Cloud Gateway，那么yml配置文件中的SwaggerHeaderFilter配置应该去掉
