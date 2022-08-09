package org.microservices.common.cache.support.redis.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

/**
 * @author xiangqian
 * @date 20:41 2022/06/29
 */
public abstract class RedisAbstractOperations {

    @Autowired
    protected RedisTemplate<Object, Object> redisTemplate;

    protected void checkKeyAndHashKey(Object key, Object hashKey) {
        checkKey(key);
        checkHashKey(hashKey);
    }

    protected void checkHashKey(Object hashKey) {
        Assert.notNull(hashKey, "hashKey不能为null");
    }

    protected void checkKey(Object key) {
        Assert.notNull(key, "key不能为null");
    }

}
