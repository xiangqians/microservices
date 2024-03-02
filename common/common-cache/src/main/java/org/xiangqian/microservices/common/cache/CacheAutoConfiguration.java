package org.xiangqian.microservices.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

/**
 * @author xiangqian
 * @date 19:38 2023/09/07
 */
@Slf4j
@EnableCaching // 开启基于注解缓存
@Configuration(proxyBeanMethods = false)
public class CacheAutoConfiguration {

    /**
     * {@link RedisAutoConfiguration}
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(30)) // 设置缓存过期时间为30分钟
                )
                .build();
    }

    @Bean
    public Cache redisCache(RedisTemplate redisTemplate) {
        return new RedisCache(redisTemplate);
    }

}
