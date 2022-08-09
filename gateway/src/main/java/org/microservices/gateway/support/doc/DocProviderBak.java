//package org.microservices.gateway.support;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.config.GatewayProperties;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Component;
//import springfox.documentation.swagger.web.SwaggerResource;
//import springfox.documentation.swagger.web.SwaggerResourcesProvider;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * doc聚合
// * <p>
// * 在我们使用Spring Boot等单体架构集成swagger项目时,是通过对包路径进行业务分组,然后在前端进行不同模块的展示,而在微服务架构下,我们的一个服务就类似于原来我们写的一个业务组
// * springfox-swagger提供的分组接口是swagger-resource,返回的是分组接口名称、地址等信息
// * 在Spring Cloud微服务架构下,我们需要重写该接口,主要是通过网关的注册中心动态发现所有的微服务文档
// * <p>
// * 使用Spring Boot单体架构集成swagger时，是通过包路径进行业务分组，然后在前端进行不同模块的展示，而在微服务架构下，单个服务类似于原来业务组；
// * springfox-swagger提供的分组接口是swagger-resource，返回的是分组接口名称、地址等信息；
// * 在Spring Cloud微服务架构下，需要swagger-resource重写接口，由网关的注册中心动态发现所有的微服务文档
// * <p>
// * 文档聚合业务编码
// * 在我们使用Spring Boot等单体架构集成swagger项目时,是通过对包路径进行业务分组,然后在前端进行不同模块的展示,而在微服务架构下,我们的一个服务就类似于原来我们写的一个业务组
// * <p>
// * springfox-swagger提供的分组接口是swagger-resource,返回的是分组接口名称、地址等信息
// * <p>
// * 在Spring Cloud微服务架构下,我们需要重写该接口,主要是通过网关的注册中心动态发现所有的微服务文档,代码如下：
// *
// * @author xiangqian
// * @date 16:38:09 2022/04/02
// */
//@Slf4j
//@Primary
//@Component
//public class DocProviderBak implements SwaggerResourcesProvider {
//
//    // swagger2默认的url后缀
//    private static final String SWAGGER2_URL = "/v2/api-docs";
//
//    // 路由定位器
//    @Autowired
//    private RouteLocator routeLocator;
//
//    @Autowired
//    private GatewayProperties gatewayProperties;
//
//    @Value("${spring.application.name}")
//    private String name;
//
////    @Override
////    public List<SwaggerResource> get() {
////        List<SwaggerResource> resources = new ArrayList<>();
////        List<String> routeHosts = new ArrayList<>();
////        // 1. 获取路由Uri 中的Host=> 服务注册则为服务名=》app-service001
////        routeLocator.getRoutes()
////                .filter(route -> route.getUri().getHost() != null)
////                .filter(route -> !name.equals(route.getUri().getHost()))
////                .subscribe(route -> routeHosts.add(route.getUri().getHost()));
////        // 2. 创建自定义资源
////        for (String routeHost : routeHosts) {
////            String serviceUrl = "/" + routeHost + SWAGGER2_URL; // 后台访问添加服务名前缀
////            SwaggerResource swaggerResource = new SwaggerResource(); // 创建Swagger 资源
////            swaggerResource.setUrl(serviceUrl); // 设置访问地址
////            swaggerResource.setName(routeHost); // 设置名称
////            swaggerResource.setSwaggerVersion("3.0.0");
////            resources.add(swaggerResource);
////        }
////        return resources;
////    }
////
//
//    @Override
//    public List<SwaggerResource> get() {
//        // 接口资源列表
//        List<SwaggerResource> resources = new ArrayList<>();
//
//        // 服务名称列表
//        List<String> routes = new ArrayList<>();
////        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
////        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId())).forEach(route -> {
////            route.getPredicates().stream()
////                    .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
////                    .forEach(predicateDefinition -> resources.add(swaggerResource(route.getId(),
////                            predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
////                                    .replace("**", "v2/api-docs"))));
////        });
//
//        // 获取所有可用的应用名称
//        routeLocator.getRoutes()
//                .filter(route -> route.getUri().getHost() != null)
//                .filter(route -> !applicationName.equals(route.getUri().getHost()))
//                .subscribe(route -> routes.add(route.getUri().getHost()));
//
//        // 去重，多负载服务只添加一次
//        Set<String> existsServer = new HashSet<>();
//        routes.forEach(host -> {
//            // 拼接url
//            String url = "/" + host + API_URI;
//            log.info("-------url {}", url);
//            //不存在则添加
//            if (!existsServer.contains(url) && host.contains("example1")) {
//                existsServer.add(url);
//                SwaggerResource swaggerResource = new SwaggerResource();
//                swaggerResource.setUrl(url);
//                swaggerResource.setName(host);
//                resources.add(swaggerResource);
//            }
//        });
//
//        return resources;
//    }
//
//    private SwaggerResource swaggerResource(String name, String location) {
//        log.info("name:{},location:{}", name, location);
//        SwaggerResource swaggerResource = new SwaggerResource();
//        swaggerResource.setName(name);
//        swaggerResource.setLocation(location);
//        swaggerResource.setSwaggerVersion("2.0");
//        return swaggerResource;
//    }
//
//    // 网关应用名称
//    @Value("${spring.application.name}")
//    private String applicationName;
//
//    //接口地址
//    private static final String API_URI = "/v2/api-docs";
//
//}
//// https://doc.xiaominfo.com/knife4j/action/springcloud-gateway.html
//// https://doc.xiaominfo.com/knife4j/action/aggregation-cloud.html
//// https://doc.xiaominfo.com/knife4j/action/springcloud-gateway.html#_2-1-3-2-application%E6%96%87%E4%BB%B6%E9%85%8D%E7%BD%AE