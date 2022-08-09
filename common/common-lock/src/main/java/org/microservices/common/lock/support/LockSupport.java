package org.microservices.common.lock.support;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.microservices.common.lock.annotation.Lock;
import org.microservices.common.lock.properties.LockProperties;
import org.microservices.common.lock.properties.LockPropertiesFactory;
import org.microservices.common.lock.provider.LockProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * {@link Lock} 支持类
 * <p>
 * {@link org.springframework.cache.annotation.Cacheable} 源码：
 * {@link org.springframework.cache.interceptor.CacheInterceptor}
 * {@link org.springframework.cache.interceptor.CacheAspectSupport#execute(org.springframework.cache.interceptor.CacheOperationInvoker, java.lang.Object, java.lang.reflect.Method, java.lang.Object[])}
 * {@link org.springframework.cache.interceptor.AbstractFallbackCacheOperationSource#getCacheOperations(java.lang.reflect.Method, java.lang.Class)}
 * <p>
 * {@link Lock} 和 {@link org.springframework.cache.annotation.Cacheable} 注解同时使用时，
 * {@link Lock} 注解于 {@link org.springframework.cache.annotation.Cacheable} 注解之前处理，从而能确保缓存击穿问题
 *
 * @author xiangqian
 * @date 15:47 2022/04/17
 */
@Aspect
public class LockSupport implements Ordered {

    @Autowired
    private LockPropertiesFactory lockPropertiesFactory;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("defaultLockProvider")
    private LockProvider defaultLockProvider;

    @Around("@annotation(org.microservices.common.lock.annotation.Lock)")
    public Object invoked(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取被代理的Class
        Class<?> targetClass = joinPoint.getClass();

        // 获取被代理对象
//        Object proxy = joinPoint.getThis();

        // 获取署名信息
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        // 获取实参对象
        Object[] args = joinPoint.getArgs();

        // 获取锁配置信息
        LockProperties lockProperties = lockPropertiesFactory.get(targetClass, method, args);

        // 获取锁提供者
        LockProvider lockProvider = null;
        // 如果没有显式的指定锁提供者，则使用默认的锁提供者
        if (StringUtils.isEmpty(lockProperties.getLockProvider())) {
            lockProvider = defaultLockProvider;
        }
        // 使用显式的指定锁提供者
        else {
            lockProvider = applicationContext.getBean(lockProperties.getLockProvider(), LockProvider.class);
        }

        //
        org.microservices.common.lock.provider.Lock lock = null;
        try {
            // 获取Lock实例
            lock = lockProvider.getLock(lockProperties);

            // lock
            lock.lock();
            return joinPoint.proceed();
        } finally {
            // unlock
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
