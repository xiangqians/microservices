package org.xiangqian.microservices.common.lock;

import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.GzipCompressionProvider;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xiangqian.microservices.common.util.DurationUtil;
import org.xiangqian.microservices.common.util.Md5Util;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author xiangqian
 * @date 22:26 2024/02/28
 */
@Data
@Aspect
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "zookeeper")
public class LockAutoConfiguration {

    // 服务器地址，多个地址以逗号分隔
    private String host;

    // 连接超时时间，时间格式：{n}h{n}m{n}s
    private String connectionTimeout;

    // 会话超时时间，时间格式：{n}h{n}m{n}s
    private String sessionTimeout;

    @Bean
    public CuratorFramework curatorFramework() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(host)
                .connectionTimeoutMs((int) DurationUtil.parse(connectionTimeout).toMillis())
                .sessionTimeoutMs((int) DurationUtil.parse(sessionTimeout).toMillis())
                // 为CuratorFramework实例设置命名空间，所有操作都会在该命名空间下进行
                .namespace("myNamespace")
                // 设置访问授权信息，可以在连接ZooKeeper时进行身份验证
//                .authorization("digest", "username:password".getBytes())
                // 设置是否允许在连接到ZooKeeper集群的时候进入只读模式
                .canBeReadOnly(false)
                // 设置数据压缩提供者，用于对数据进行压缩
                .compressionProvider(new GzipCompressionProvider())
                // 设置用于创建Curator使用的线程的工厂
//                .threadFactory()
                // 重试策略，在与ZooKeeper服务器交互时发生错误后的重试行为
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        curatorFramework.start();
        return curatorFramework;
    }

    @Autowired
    private CuratorFramework curatorFramework;

    // 根据变量值来获取锁？-- 待实现

    @Around("@annotation(org.xiangqian.microservices.common.lock.Lock)")
    public Object around(ProceedingJoinPoint joinPoint, Lock lock) throws Throwable {
        // InterProcessMutex是Curator 提供的基本分布式锁实现，它提供了互斥锁的语义，即同一时刻只有一个客户端能够获取到锁。
        // 当一个客户端持有该锁时，其他客户端请求锁会被阻塞，直到持有锁的客户端释放锁为止。
        InterProcessMutex interProcessMutex = null;
        try {
            // 获取锁
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            String path = "/" + Md5Util.encryptHex(joinPoint.getTarget().getClass().getName() + "#" + method.getName());
            interProcessMutex = new InterProcessMutex(curatorFramework, path);
            if (!interProcessMutex.acquire(lock.timeout(), TimeUnit.MILLISECONDS)) {
                throw new InterruptedException("无法获取锁");
            }

            return joinPoint.proceed();
        } finally {
            // 在Curator的InterProcessMutex类中，isOwnedByCurrentThread()方法和isAcquiredInThisProcess()方法都是用于检查当前线程或进程是否已经获取了特定的分布式锁。
            // 1、isOwnedByCurrentThread()方法用于检查当前线程是否已经获取了指定的分布式锁。
            // 如果当前线程已经成功获取了该分布式锁，则该方法返回 true，表示当前线程拥有该锁；
            // 如果当前进线程有获取该分布式锁，或者之前获取的锁已经被释放，那么该方法会返回 false。
            // 这个方法主要用于检查当前线程是否持有该分布式锁，在多线程环境中可以帮助你确保某些操作只能由持有该锁的线程执行。
            // 2、isAcquiredInThisProcess()方法用于检查当前进程是否已经获取了指定的分布式锁。具体来说，这个方法会检查当前进程是否已经成功获取了InterProcessMutex对象所表示的分布式锁。
            // 如果当前进程已经成功获取了该分布式锁，则该方法返回true，表示当前进程拥有该锁；
            // 如果当前进程没有获取该分布式锁，或者之前获取的锁已经被释放，那么该方法会返回 false。
            // 这个方法与isOwnedByCurrentThread()方法类似，不同之处在于它是针对进程而非线程的。它可以用于检查当前进程是否持有特定的分布式锁。
            if (interProcessMutex.isAcquiredInThisProcess()) {
                // 释放锁
                interProcessMutex.release();
            }
        }
    }

}
