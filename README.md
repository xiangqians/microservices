# 简介

微服务架构

- JDK17
- Maven v3.6.0
- Spring Boot
- Spring Authorization Server
- Cache
- ...

# 项目目录结构

```text
├ microservices
├ 
├── common                      （公共模块）
├    ├── common-auth            （授权资源模块）
├    ├── common-cache           （缓存模块）
├    ├── common-codegen         （代码生成模块）
├    ├── common-db              （db模块）
├    ├── common-model           （模型模块）
├    ├── common-register-factory（注册中心工厂模块，支持consul和nacos）
├    ├── common-rpc             （远程过程调用协议模块）
├    ├── common-thread-pool     （线程池模块）
├    ├── common-util            （工具模块）
├    ├── common-webflux         （webflux模块）
├    └── common-webmvc          （webmvc模块）
├── user                        （用户模块）
├    ├── user-model             （用户模型模块）
├    ├── user-api               （用户API模块）
├    └── user-biz               （用户业务服务模块）
├── order                       （订单模块）
├    ├── order-model            （订单模型模块）
├    ├── order-api              （订单API模块）
├    └── order-biz              （订单业务服务模块）
├── auth                        （授权认证模块）
└── gateway                     （网关服务模块）
```

任务调度中心？

# API文档

[Swagger](http://localhost:8000/swagger-ui.html)

[Knife4j](http://localhost:8000/doc.html)

# 微服务间通信

- 同步通信：RPC
- 异步通信：MQ
