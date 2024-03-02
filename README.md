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
├    ├── common-cache           （缓存模块）
├    ├── common-code-generator  （代码生成器模块）
├    ├── common-db              （数据库模块）
├    ├── common-lock            （分布式锁模块）
├    ├── common-model           （模型模块）
├    ├── common-monitor         （监控模块）
├    ├── common-mq              （消息队列模块）
├    ├── common-oss             （对象存储服务模块）
├    ├── common-register        （注册中心模块）
├    ├── common-rpc             （远程过程调用协议模块）
├    ├── common-thread-pool     （线程池模块）
├    ├── common-util            （工具模块）
├    ├── common-webflux         （webflux模块）
├    ├── common-webflux-auth    （webflux权限模块）
├    ├── common-webmvc          （webmvc模块）
├    └── common-webmvc-auth     （webmvc权限模块）
├── user                        （用户模块）
├    ├── user-model             （用户模型模块）
├    ├── user-api               （用户API模块）
├    └── user-biz               （用户业务服务模块）
├── order                       （订单模块）
├    ├── order-model            （订单模型模块）
├    ├── order-api              （订单API模块）
├    └── order-biz              （订单业务服务模块）
├── auth                        （授权认证模块）
├── monitor                     （监控模块）
└── gateway                     （网关服务模块）

分布式事务
分布式日志服务

```

考虑：

- 服务间调用token传递（结合jwt？）

任务调度中心？

# API文档

[Swagger](http://localhost:8000/swagger-ui.html)

[Knife4j](http://localhost:8000/doc.html)

# 微服务间通信

- 同步通信：RPC
- 异步通信：MQ

# rest风格

做只读权限管理时，只放行Get请求即可。

# 主键id（int）

-1 标识删除

```
`xx_id`      INT(8) UNSIGNED DEFAULT 0 COMMENT 'id',
`name`           VARCHAR(256)      DEFAULT '' COMMENT 'name',
`number`               DECIMAL(14, 4)    DEFAULT NULL COMMENT 'number', -- 默认设置为NULL，因为用户有时候输入空串（""），用NULL表示
```

``` 
建议，每个entity对应一个vo，比如number数据库类型是int类型，但是前端输入空串
```
