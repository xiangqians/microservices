# microservices

```
├ microservices
├ 
├── common              （公共模块）
├    ├── common-core    （公共核心模块）
├    ├── common-auth    （公共权限模块）
├    ├── common-cache   （公共缓存模块）
├    ├── common-db      （公共db模块）
├    ├── common-doc     （公共文档模块，knife4j）
├    ├── common-feign   （公共feign模块，需要使用@EnableFeign注解开启）
├    ├── common-hystrix （公共hystrix模块）
├    ├── common-monitor （公共监控模块）
├    ├── common-register（公共服务注册与配置模块）
├    └── common-web     （公共web模块）
├── register            （服务注册与配置中心， nacos [8848]）
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
