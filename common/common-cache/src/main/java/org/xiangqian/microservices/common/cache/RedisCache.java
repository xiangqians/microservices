package org.xiangqian.microservices.common.cache;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * redis客户端操作
 *
 * @author xiangqian
 * @date 21:33 2024/02/28
 */
public class RedisCache implements Cache {

    private RedisTemplate redisTemplate;

    public RedisCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Boolean set(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    @Override
    public Boolean setIfAbsent(Object key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public Boolean expire(Object key, Duration timeout) {
        return redisTemplate.expire(key, timeout.getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public <V> V get(Object key) {
        ValueOperations<Object, V> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    @Override
    public Boolean delete(Object key) {
        return redisTemplate.delete(key);


        //哈希操作
        //java
        //// 设置哈希值

        //// 获取哈希值
        //Object value = redisTemplate.opsForHash().get(hashKey, key);
        //
        //// 删除哈希值
        //redisTemplate.opsForHash().delete(hashKey, key);
    }

    @Override
    public Hash hash(Object key) {
        return new Hash() {
            @Override
            public Boolean set(Object hashKey, Object hashValue) {
                redisTemplate.opsForHash().put(key, hashKey, hashValue);
                return true;
            }

            @Override
            public <HV> HV get(Object hashKey) {
                HashOperations<Object, Object, HV> hashOperations = redisTemplate.opsForHash();
                return hashOperations.get(key, hashKey);
            }

            @Override
            public Boolean delete(Object hashKey) {
                redisTemplate.opsForHash().delete(key, hashKey);
                return true;
            }

            @Override
            public Boolean hasKey(Object hashKey) {
                return redisTemplate.opsForHash().hasKey(key, hashKey);
            }
        };
    }

    @Override
    public List list(Object key) {
        return null;
    }

}
