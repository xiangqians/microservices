package org.microservices.common.cache;

import org.microservices.common.cache.support.caffeine.CaffeineSupport;
import org.microservices.common.cache.support.ehcache.EhcacheSupport;
import org.microservices.common.cache.support.redis.RedisSupport;
import org.microservices.common.core.annotation.YamlSource;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Ehcache
 *
 * @author xiangqian
 * @date 19:20 2022/04/07
 */
@Configuration(proxyBeanMethods = false)
@EnableCaching // 开启基于注解缓存
@ImportAutoConfiguration({CaffeineSupport.class, EhcacheSupport.class, RedisSupport.class})
@YamlSource({"classpath:common-cache.yml"})
public class CacheAutoConfiguration {

}
