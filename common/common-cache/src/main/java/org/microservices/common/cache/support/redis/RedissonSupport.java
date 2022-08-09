package org.microservices.common.cache.support.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95
 *
 * @author xiangqian
 * @date 19:15 2022/04/17
 */
//@Configuration
//@Conditional(value = "redis", processor = CacheSupportConditionProcessor.class)
public class RedissonSupport {

    @Bean
    public RedissonClient redissonClient(RedisProperties properties) throws IOException {
        Config config = new Config();
        config.useSingleServer()
                // 可以用"rediss://"来启用SSL连接
                .setAddress(String.format("redis://%s:%d",
                        "192.168.194.132",//properties.getHost(),
                        properties.getPort()));
        return Redisson.create(config);
    }

}
