package org.microservices.common.cache.support.redis.operation;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author xiangqian
 * @date 23:53 2022/06/28
 */
public class RedisKeyOperations extends RedisAbstractOperations {

    /**
     * 指定key缓存失效时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(Object key, long timeout, TimeUnit unit) {
        if (Objects.isNull(key)) {
            return false;
        }
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取key过期时间
     *
     * @param key
     * @param timeUnit
     * @return 返回0表示永久有效
     */
    public Long getExpire(Object key, TimeUnit timeUnit) {
        checkKey(key);
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public Boolean hasKey(Object key) {
        if (Objects.isNull(key)) {
            return false;
        }
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除keys
     *
     * @param keys
     */
    public Boolean delete(Object... keys) {
        if (ArrayUtils.isEmpty(keys)) {
            return false;
        }

        if (keys.length == 0) {
            Object key = keys[0];
            if (key != null) {
                redisTemplate.delete(key);
                return true;
            }
            return false;
        }

        List<Object> keyList = new ArrayList<>(keys.length);
        for (Object key : keys) {
            if (key != null) {
                keyList.add(key);
            }
        }
        if (CollectionUtils.isEmpty(keyList)) {
            return false;
        }
        redisTemplate.delete(keyList);
        return true;
    }

}
