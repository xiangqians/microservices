# 部署Consul

## 下载

[Install Consul](https://developer.hashicorp.com/consul/downloads)

Install or update to v1.16.1 (latest version) of Consul to get started.

## 单机部署

```shell
$ consul -help
```

```shell
#$ consul agent -dev -client=0.0.0.0
```

```shell
#$ consul agent -server -ui -node=node -bootstrap-expect=1 -data-dir=/tmp/consul -config-dir=/etc/consul.d -client=0.0.0.0 -bind=0.0.0.0
$ consul agent -server -ui -node=node -bootstrap-expect=1 -data-dir=./data/consul.d -log-file=./log/consul.log -client=0.0.0.0 -bind=192.168.2.10
```

```
2023-09-21T10:55:19.147+0800 [ERROR] agent: startup error: error="refusing to rejoin cluster because server has been offline for more than the configured server_rejoin_age_max (168h0m0s) - consider wiping your data dir"

大意是说这个节点离线时间太长了，让擦除数据目录。于是又去查了查Issue和文档，解决方案是删除数据目录下的server_metadata.json文件可以强制重新加入。具体可以看最后的参考链接。
```




参数说明：

- `agent`表示启动一个Agent进程
- `-dev`表示开发模式运行
- `-server`表示服务模式运行
- `-ui`表示开启可视化管理界面
- `-node`指定节点名称，注意：每个节点的名称必须唯一
- `-bootstrap-expect`指定集群中服务器节点的期望数量，当节点数量不足时会等待，直到达到期望数量才会启动。单机部署，设定为1即可
- `-data-dir`指定数据目录的位置
- `-config-dir`指定配置文件的位置
- `-client=0.0.0.0`表示允许所有ip访问
- `-bind`绑定地址，该地址用于监听服务请求，默认为0.0.0.0

[可视化管理界面](http://localhost:8500)

## 集群部署
