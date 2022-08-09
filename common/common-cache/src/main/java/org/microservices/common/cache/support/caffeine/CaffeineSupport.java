package org.microservices.common.cache.support.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * spring-context-support-5.3.19.jar
 * {@link CaffeineCache}
 *
 * @author xiangqian
 * @date 23:01 2022/07/04
 */
@Configuration
@ConditionalOnProperty(value = "microservices.cache.type", havingValue = "caffeine", matchIfMissing = true)
public class CaffeineSupport {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 初始缓存容量
                .initialCapacity(1024)
                // 设置写后过期时间
//                .expireAfterWrite(Duration.ofMinutes(1))
                // 设置访问后过期时间
                .expireAfterAccess(Duration.ofMinutes(1))
                // 设置cache的最大缓存数量
                .maximumSize(1024 * 1024));

        // 设置缓存加载器
        cacheManager.setCacheLoader(key -> null);

        // 不允许空值
        cacheManager.setAllowNullValues(false);

        return cacheManager;
    }

}
