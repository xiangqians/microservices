# 简介

http://localhost:9001/swagger-ui.html

http://localhost:9001/doc.html

# 项目目录结构

```
├ microservices
├ 
├── common              （公共模块）
├    ├── common-core    （公共核心模块）
├    ├── common-auth    （公共权限模块）
├    ├── common-cache   （公共缓存模块）
├    ├── common-db      （公共db模块）
├    ├── common-doc     （公共文档模块，springdoc + knife4j）
├    ├── common-declarative-rest-client   （声明式REST客户端模块，需要使用@EnableFeign注解开启）
├    ├── common-circuit-breaker （公共断路器模块） --> Resilience4j
├    ├── common-monitor （公共监控模块）
├    ├── common-register（公共服务注册与配置模块）
├    └── common-web     （公共web模块）
├── register            （服务注册与配置中心， consul [8500]）
├── auth                （授权服务 [3000]）
├── user                （用户中心 [9000]）
├    ├── user-common    （用户公共模块）
├    ├── user-api       （用户API服务）
├    ├── user-biz       （用户业务服务）
├── order               （订单中心 [9400]）
├    ├── order-common   （订单公共模块）
├    ├── order-api      （订单API服务）
├    ├── order-biz      （订单业务服务）
├── monitor             （监控服务 [9200]）
└── gateway             （网关服务 [9999]）
```

Hystrix Dashboard / Turbine -> Micrometer + Monitoring System

# 各个服务模块介绍

## register（服务注册与配置中心）

1. 安装并启动nacos服务
2. 根据当前 @profiles.active@（于pom.xml中<profiles>定义）环境标识，在nacos web管理界面创建命名空间（Namespace），Namespace ID为 @profiles.active@

## auth（授权服务）

oauth2

## gateway （网关服务）

[test](conf/gateway.json)

#

https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/README-zh.md

#                                                                                                                                                             

nohup command >/dev/null 2>&1 &

# Skywalking

java -javaagent:/usr/local/skywalking/apache-skywalking-apm-bin-es7/agent/skywalking-agent.jar       
-Dskywalking.agent.service_name=user-provider2 -Dskywalking.collector.backend_service=192.168.3.169:11800
user-provider-0.0.1-SNAPSHOT.jar

-javaagent:C:\Users\xiangqian\Desktop\tmp\skywalking-agent\skywalking-agent.jar -Dskywalking.agent.service_name=auth-biz
-Dskywalking.collector.backend_service=127.0.0.1:11800 -javaagent:C:
\Users\xiangqian\Desktop\tmp\skywalking-agent\skywalking-agent.jar -Dskywalking.agent.service_name=user-biz
-Dskywalking.collector.backend_service=127.0.0.1:11800 -jar xxx.jar





