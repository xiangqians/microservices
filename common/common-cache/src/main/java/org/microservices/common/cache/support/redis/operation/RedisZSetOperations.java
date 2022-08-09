package org.microservices.common.cache.support.redis.operation;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Objects;
import java.util.Set;

/**
 * 有序Set操作
 *
 * @author xiangqian
 * @date 20:39 2022/06/29
 */
public class RedisZSetOperations extends RedisAbstractOperations {

    /**
     * @param key
     * @param value
     * @param score 分数
     * @return
     */
    public Boolean add(Object key, Object value, double score) {
        if (Objects.isNull(key)) {
            return false;
        }
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * @param key
     * @param tuples
     * @return
     */
    public Long add(Object key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        if (Objects.isNull(key)) {
            return 0L;
        }
        return redisTemplate.opsForZSet().add(key, tuples);
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
        return redisTemplate.opsForZSet().remove(key, values);
    }


    /**
     * 为指定元素加分
     *
     * @param key
     * @param value
     * @param delta
     * @return 返回加分后的得分
     */
    public Double incrementScore(Object key, Object value, double delta) {
        if (Objects.isNull(key)) {
            return 0D;
        }
        return redisTemplate.opsForZSet().incrementScore("ranking-list", "p1", 2);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Long rank(Object key, Object value) {
        checkKey(key);
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Long reverseRank(Object key, Object value) {
        checkKey(key);
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(Object key, long start, long end) {
        checkKey(key);
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().rangeWithScores(key, start, end);
        return tuples;
    }

    /**
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> rangeByScore(Object key, double min, double max) {
        if (Objects.isNull(key)) {
            return null;
        }
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<Object> rangeByScore(Object key, double min, double max, long offset, long count) {
        if (Objects.isNull(key)) {
            return null;
        }
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long countByScore(Object key, double min, double max) {
        if (Objects.isNull(key)) {
            return 0L;
        }
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * @param key
     * @return
     */
    public Long size(Object key) {
        if (Objects.isNull(key)) {
            return 0L;
        }
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Double score(Object key, Object value) {
        if (Objects.isNull(key)) {
            return 0D;
        }
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long removeRange(Object key, long start, long end) {
        if (Objects.isNull(key)) {
            return 0L;
        }
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long removeRangeByScore(Object key, double min, double max) {
        if (Objects.isNull(key)) {
            return 0L;
        }
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    public Set<Object> range(Object key, long start, long end) {
        if (Objects.isNull(key)) {
            return null;
        }
        return redisTemplate.opsForZSet().range(key, start, end);
    }

}
