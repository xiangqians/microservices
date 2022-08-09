package org.microservices.common.cache.support.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.microservices.common.cache.support.redis.operation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

/**
 * Redis缓存支持
 * <p>
 * spring-boot-autoconfigure将会自动注入 {@link RedisConnectionFactory} bean，源码如下：
 * spring-data-redis-2.6.4.jar!\META-INF\spring.factories
 * spring-boot-autoconfigure-2.6.7.jar!\META-INF\spring.factories
 * {@link RedisAutoConfiguration}
 * {@link org.springframework.boot.autoconfigure.data.redis.LettuceConnectionConfiguration}
 * {@link org.springframework.boot.autoconfigure.data.redis.JedisConnectionConfiguration}
 * Lettuce和Jedis的都是连接 Redis Server的客户端，redis自动配置使用Lettuce作为client进行连接
 *
 * @author xiangqian
 * @date 22:30 2022/04/07
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "microservices.cache.type", havingValue = "redis")
public class RedisSupport {//extends CachingConfigurerSupport {

    @Bean
    public RedisMapOperations redisMapOperations() {
        return new RedisMapOperations();
    }

    @Bean
    public RedisZSetOperations redisZSetOperations() {
        return new RedisZSetOperations();
    }

    @Bean
    public RedisSetOperations redisSetOperations() {
        return new RedisSetOperations();
    }

    @Bean
    public RedisListOperations redisListOperations() {
        return new RedisListOperations();
    }

    @Bean
    public RedisStringOperations redisStringOperations() {
        return new RedisStringOperations();
    }

    @Bean
    public RedisKeyOperations redisKeyOperations() {
        return new RedisKeyOperations();
    }

    // KEY生成器
//    @Bean
//    @Override
//    public KeyGenerator keyGenerator() {
//        return (Object target, Method method, Object... params) -> String.format("%s", method.getName(), DigestUtils.md5Hex(Arrays.toString(params)));
//    }

    /**
     * 缓存管理器
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存有效期6m
                .entryTtl(Duration.ofMinutes(6))
                // 设置key序列化器
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 设置value序列化器
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(createJackson2JsonRedisSerializer(objectMapper.copy())))
                // 禁用缓存空值
                .disableCachingNullValues();
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                // 配置同步修改或删除put/evict
                .transactionAware()
                .cacheDefaults(configuration)
                .build();
    }

    /**
     * 覆盖默认的 {@link StringRedisTemplate}
     * * {@link RedisAutoConfiguration#stringRedisTemplate(org.springframework.data.redis.connection.RedisConnectionFactory)}
     * *
     *
     * @param redisConnectionFactory
     * @return
     */
//    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    /**
     * 覆盖默认的 {@link RedisTemplate}
     * {@link RedisAutoConfiguration#redisTemplate(org.springframework.data.redis.connection.RedisConnectionFactory)}
     *
     * @param redisConnectionFactory
     * @return
     */
//    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        // 创建redisTemplate
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        // RedisConnectionFactory
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize替换默认序列化
        RedisSerializer<Object> redisSerializer = createJackson2JsonRedisSerializer(objectMapper);
//        RedisSerializer<Object> redisSerializer = createGenericJackson2JsonRedisSerializer();

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // set value serializer
        redisTemplate.setDefaultSerializer(redisSerializer);
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(redisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(redisSerializer);
        //
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    private RedisSerializer<Object> createGenericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    private RedisSerializer<Object> createJackson2JsonRedisSerializer(ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会报异
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.disable(MapperFeature.AUTO_DETECT_SETTERS);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return jackson2JsonRedisSerializer;
    }

}
