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
├    ├── common-register-consul （consul注册中心工厂模块）
├    ├── common-register-nacos  （nacos注册中心工厂模块）
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

分布式事务
文件上传工具类
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

# 代码生成器+

- 生成Entity时，属性添加校验器 @Length(max = xxx, message = "xxx长度不能超过xxx个字符")
- 生成Entity时，添加trim方法，为每个属性去除前后空格
``` 
// 废除
public void trim(boolean toNull) {
    Function<String, String> trimFunc = str -> toNull ? StringUtils.trimToNull(str) : StringUtils.trim(str);
    name = trimFunc.apply(name);
}

// ?
public void post() {
}

public void setName(String name) {
    this.name = StringUtils.trim(name);
}

所以，建议，每个entity对应一个vo，比如number数据库类型是int类型，但是前端输入空串
```
