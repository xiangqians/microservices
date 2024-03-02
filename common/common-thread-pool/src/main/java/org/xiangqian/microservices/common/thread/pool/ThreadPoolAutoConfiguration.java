package org.xiangqian.microservices.common.thread.pool;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xiangqian.microservices.common.util.DurationUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

/**
 * @author xiangqian
 * @date 19:28 2024/02/27
 */
@Data
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "thread.pool")
@ConditionalOnProperty(name = "thread.pool.core-pool-size") // 当属性 thread.pool.core-pool-size 存在时加载配置类
public class ThreadPoolAutoConfiguration {

    // 线程池中核心线程数量
    // 线程池中始终保持存活的线程数量，即使这些线程处于空闲状态，也不会被回收
    private int corePoolSize;

    // 线程池中最大线程数量
    // 线程池中允许存在的最大线程数量，当工作队列已满且当前线程数小于最大线程数时，会创建新线程来处理任务
    private int maximumPoolSize;

    // 线程池中非核心线程闲置超时时间，时间格式：{n}h{n}m{n}s
    private String keepAliveTime;

    // 工作队列容量
    private int workQueueCapacity;

    // 拒绝策略，用于定义当工作队列和线程池都满了之后的处理方式
    // 常见的策略包括：
    // java.util.concurrent.ThreadPoolExecutor$AbortPolicy      : 抛出RejectedExecutionException
    // java.util.concurrent.ThreadPoolExecutor$CallerRunsPolicy : 由调用线程处理任务
    // java.util.concurrent.ThreadPoolExecutor$DiscardPolicy    : 默默丢弃任务
    private String rejectedExecutionHandlerClassName;

    @Bean
    public ExecutorService executorService() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("线程池配置信息" +
                        "\n\t线程池中核心线程数量：{}" +
                        "\n\t线程池中最大线程数量：{}" +
                        "\n\t线程池中非核心线程闲置超时时间：{}" +
                        "\n\t工作队列容量：{}" +
                        "\n\t拒绝策略：{}",
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                workQueueCapacity,
                rejectedExecutionHandlerClassName);
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                DurationUtil.parse(keepAliveTime).getSeconds(),
                TimeUnit.SECONDS, // 非核心线程闲置超时时间单位
                new LinkedBlockingQueue<>(workQueueCapacity), // 工作队列，用于存放等待执行的任务
                Executors.defaultThreadFactory(), // 线程工厂，用于创建新线程
                (RejectedExecutionHandler) Class.forName(rejectedExecutionHandlerClassName).getConstructor().newInstance());
    }

}
