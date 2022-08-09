package org.microservices.common.lock.annotation;

import org.microservices.common.lock.enumeration.LockType;
import org.microservices.common.lock.provider.LockProvider;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xiangqian
 * @date 21:47 2022/04/16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

    /**
     * 锁名称，支持SpEL
     *
     * @return
     */
    String lockName();

    /**
     * 锁类型
     *
     * @return
     */
    LockType lockType() default LockType.UNFAIR_LOCK;

    /**
     * 尝试获取锁等待时间
     *
     * @return
     */
    long waitTime() default 10;

    /**
     * 租期，-1表示永久
     *
     * @return
     */
    long leaseTime() default 60;

    /**
     * 时间单位
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 锁的提供者，将会从IOC容器中获取
     * see {@link LockProvider}
     *
     * @return
     */
    String lockProvider() default "";

}
