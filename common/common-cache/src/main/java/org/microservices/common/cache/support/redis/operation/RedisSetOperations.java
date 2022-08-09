package org.microservices.common.cache.support.redis.operation;

import java.util.Objects;
import java.util.Set;

/**
 * 无序Set操作
 *
 * @author xiangqian
 * @date 00:22 2022/06/29
 */
public class RedisSetOperations extends RedisAbstractOperations {

    /**
     * @param key
     * @return
     */
    public Set<Object> get(Object key) {
        if (Objects.isNull(key)) {
            return null;
        }
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Boolean hasKey(Object key, Object value) {
        if (Objects.isNull(key)) {
            return false;
        }
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * @param key
     * @param values
     * @return
     */
    public Long add(Object key, Object... values) {
        if (Objects.isNull(key)) {
            return 0L;
        }
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * @param key
     * @return
     */
    public Long size(Object key) {
        if (Objects.isNull(key)) {
            return 0L;
        }
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * @param key
     * @param values
     * @return
     */
    public Long remove(Object key, Object... values) {
        if (Objects.isNull(key)) {
            return 0L;
        }
        return redisTemplate.opsForSet().remove(key, values);
    }

}
