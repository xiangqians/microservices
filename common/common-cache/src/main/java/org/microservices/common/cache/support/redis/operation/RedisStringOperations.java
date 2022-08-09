package org.microservices.common.cache.support.redis.operation;

import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 字符串操作
 *
 * @author xiangqian
 * @date 23:54 2022/06/28
 */
public class RedisStringOperations extends RedisAbstractOperations {

    /**
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(Object key) {
        if (Objects.isNull(key)) {
            return null;
        }
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Boolean set(Object key, Object value) {
        if (Objects.isNull(key)) {
            return false;
        }
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    /**
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean set(Object key, Object value, long timeout, TimeUnit unit) {
        if (Objects.isNull(key)) {
            return false;
        }
        redisTemplate.opsForValue().set(key, value, timeout, unit);
        return true;
    }

    /**
     * 递增
     *
     * @param key
     * @param delta 递增因子
     * @return
     */
    public Long increment(Object key, long delta) {
        checkKey(key);
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key
     * @param delta 递减因子
     * @return
     */
    public Long decrement(Object key, long delta) {
        checkKey(key);
        return redisTemplate.opsForValue().decrement(key, delta);
    }


    /**
     * 当key不存在时才set值
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean setnx(Object key, Object value, long timeout, TimeUnit unit) {
        // SETNX命令对应java setIfAbsent方法
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    @Deprecated
    public Boolean deleteV1(Object key, Object value) {
        Object currentValue = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(currentValue) && currentValue.equals(value)) {
            redisTemplate.opsForValue().getOperations().delete(key);
        }
        return false;
    }

    /**
     * del，如果当前key与目标值相等则进行删除
     * <p>
     * 【原子性操作】
     * 使用LUA脚本，在eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行，并且直到eval命令执行完成，Redis才会执行其他命令。
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean deleteV2(Object key, Object value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Object> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType(Object.class);
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(key), value);
        return "1".equals(String.valueOf(result));
    }

    //redis2.0版本之后setnx和expire两个命令合并为一个原子操作避免了一些极端情况其他节点无法获取锁的情况，当然用redis做分布式锁还有一些其他场景和问题需要解决，比如：
    //
    //如果在设定的超时时间之内，拿到锁服务A未处理完毕，Redis在超时后自动释放锁，其他进程B就可以拿到锁，在进程B拿到锁后，进程A执行完毕，调用del会造成误删锁。
    // 所以在执行del的时候，还需要判断是不是自己的锁;
    //A服务没有执行完毕，B服务就获得了锁，这不符合分布式锁的逻辑，解决这个问题，那么客户端就需要开启一个守护线程，在超时之前如果服务还未处理完毕，要定时给锁续命。

}
