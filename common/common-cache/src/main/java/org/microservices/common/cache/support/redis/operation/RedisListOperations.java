package org.microservices.common.cache.support.redis.operation;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * List操作
 *
 * @author xiangqian
 * @date 20:35 2022/06/29
 */
public class RedisListOperations extends RedisAbstractOperations {

    /**
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> get(Object key, long start, long end) {
        if (Objects.isNull(key)) {
            return null;
        }
        return redisTemplate.opsForList().range(key, start, end);
    }

    public List<Object> get(Object key) {
        return get(key, 0, -1);
    }

    /**
     * @param key
     * @param index index = 0, 1, ...
     *              index = -1, 倒数第一个元素
     *              index = -2, 倒数第二个元素
     * @return
     */
    public Object get(Object key, long index) {
        if (Objects.isNull(key)) {
            return null;
        }
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * @param key
     * @return
     */
    public Long size(Object key) {
        if (Objects.isNull(key)) {
            return null;
        }
        return redisTemplate.opsForList().size(key);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Boolean add(Object key, Object value) {
        if (Objects.isNull(key)) {
            return false;
        }
        redisTemplate.opsForList().rightPush(key, value);
        return true;
    }

    /**
     * @param key
     * @param values
     * @return
     */
    public Boolean addAll(Object key, Collection<Object> values) {
        if (Objects.isNull(key)) {
            return false;
        }
        redisTemplate.opsForList().rightPushAll(key, values);
        return true;
    }

    /**
     * @param key
     * @param index
     * @param value
     * @return
     */
    public Boolean set(Object key, long index, Object value) {
        if (Objects.isNull(key)) {
            return false;
        }
        redisTemplate.opsForList().set(key, index, value);
        return true;
    }

    /**
     * 删除N个值为value
     *
     * @param key
     * @param count
     * @param value
     * @return 已删除个数
     */
    public Long remove(Object key, long count, Object value) {
        if (Objects.isNull(key)) {
            return 0L;
        }
        return redisTemplate.opsForList().remove(key, count, value);
    }

}
