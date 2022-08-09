package org.microservices.common.cache.support.redis.operation;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Hash操作
 *
 * @author xiangqian
 * @date 00:06 2022/06/29
 */
public class RedisMapOperations extends RedisAbstractOperations {

    /**
     * @param key
     * @param hashKey
     * @return
     */
    public Object get(Object key, String hashKey) {
        if (ObjectUtils.anyNull(key, hashKey)) {
            return null;
        }
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * @param key
     * @return <hashKey, value>
     */
    public Map<Object, Object> get(Object key) {
        if (Objects.isNull(key)) {
            return null;
        }
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @param key
     * @param map
     * @return
     */
    public Boolean put(Object key, Map<String, Object> map) {
        if (Objects.isNull(key)) {
            return false;
        }
        redisTemplate.opsForHash().putAll(key, map);
        return true;
    }

    /**
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    public Boolean put(Object key, String hashKey, Object value) {
        if (ObjectUtils.anyNull(key, hashKey)) {
            return false;
        }
        redisTemplate.opsForHash().put(key, hashKey, value);
        return true;
    }

    /**
     * @param key
     * @param hashKeys
     */
    public Boolean remove(Object key, String... hashKeys) {
        if (Objects.isNull(key) || ArrayUtils.isEmpty(hashKeys)) {
            return false;
        }

        List<String> hashKeyList = new ArrayList<>(hashKeys.length);
        for (String hashKey : hashKeys) {
            if (hashKey != null) {
                hashKeyList.add(hashKey);
            }
        }
        if (CollectionUtils.isEmpty(hashKeyList)) {
            return false;
        }
        redisTemplate.opsForHash().delete(key, hashKeyList.toArray(String[]::new));
        return true;
    }

    /**
     * @param key
     * @param hashKey
     * @return
     */
    public Boolean hasKey(Object key, String hashKey) {
        if (ObjectUtils.anyNull(key, hashKey)) {
            return false;
        }
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    public long increment(Object key, String hashKey, long delta) {
        checkKeyAndHashKey(key, hashKey);
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    public double increment(Object key, String hashKey, double delta) {
        checkKeyAndHashKey(key, hashKey);
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

}
