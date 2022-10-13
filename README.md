# 简介

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
├    ├── common-feign   （声明式REST客户端模块，需要使用@EnableFeign注解开启）
├    ├── common-test    （测试模块）
├    ├── common-register（公共服务注册与配置模块）
├    └── common-web     （公共web模块）
├── register            （服务注册与配置中心， consul [8500]）
├── auth                （授权服务 [3000]）
├── user                （用户中心 [9000]）
├    ├── user-common    （用户公共模块）
├    ├── user-api       （用户API服务）
├    ├── user-biz       （用户业务服务）
└── gateway             （网关服务 [9999]）
```

## register（服务注册与配置中心）

```java
/**
 * 获取kv
 * {@link KeyValueConsulClient#getKVValues(java.lang.String, java.lang.String, com.ecwid.consul.v1.QueryParams)}
 * {@link ConsulPropertySources#createPropertySource(java.lang.String, com.ecwid.consul.v1.ConsulClient,
        java.util.function.BiConsumer)}
 * <p>
 * $ mkdir -p ./data/consul.d
 * $ mkdir ./log
 * $ ./consul agent -server -ui -node=consul-server -client=0.0.0.0 -bind=192.168.194.132 -bootstrap-expect=1
 -data-dir=./data/consul.d -log-file=./log/consul.log
 *
 * @author xiangqian
 * @date 14:28 2022/04/30
 */
public class RegisterApplication {
}
```

## auth（授权服务）

## gateway （网关服务）

[test](conf/gateway.json)

## Doc

http://localhost:9001/swagger-ui.html

http://localhost:9001/doc.html




