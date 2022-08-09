package org.microservices.register;

import com.ecwid.consul.v1.kv.KeyValueConsulClient;
import org.springframework.cloud.consul.config.ConsulPropertySources;

/**
 * 获取kv
 * {@link KeyValueConsulClient#getKVValues(java.lang.String, java.lang.String, com.ecwid.consul.v1.QueryParams)}
 * {@link ConsulPropertySources#createPropertySource(java.lang.String, com.ecwid.consul.v1.ConsulClient, java.util.function.BiConsumer)}
 * <p>
 * $ mkdir -p ./data/consul.d
 * $ mkdir ./log
 * $ ./consul agent -server -ui -node=consul-server -client=0.0.0.0 -bind=192.168.194.132 -bootstrap-expect=1 -data-dir=./data/consul.d -log-file=./log/consul.log
 *
 * @author xiangqian
 * @date 14:28 2022/04/30
 */
public class RegisterApplication {
}
