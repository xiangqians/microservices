package org.xiangqian.microservices.common.thread.pool;

import org.xiangqian.microservices.common.util.DateUtil;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xiangqian
 * @date 19:19 2024/02/27
 */
public class ThreadPoolTest {

    public static void main(String[] args) throws Exception {
        ThreadPoolAutoConfiguration configuration = new ThreadPoolAutoConfiguration();
        // 获取当前系统处理器数量
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.format("获取当前系统处理器数量：%s", processors).println();

        // 一般推荐将核心线程数设置为处理器数量的1-2倍
        configuration.setCorePoolSize(processors);

        // 一般情况下，可以将最大线程数设置为核心线程数的两倍或更多，视具体情况而定
        configuration.setMaximumPoolSize(configuration.getCorePoolSize() * 2);

        configuration.setKeepAliveTime("10s");
        configuration.setWorkQueueCapacity(8);
        configuration.setRejectedExecutionHandlerClassName("java.util.concurrent.ThreadPoolExecutor$AbortPolicy");
        ExecutorService executorService = configuration.executorService();

        AtomicBoolean flag = new AtomicBoolean(true);
        new Thread(() -> {
            while (flag.get()) {
                try {
                    System.out.println(executorService);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // execute
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.format("%s", index).println();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // submit
        Future<String> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return DateUtil.DateTime.format(LocalDateTime.now(), "yyyy/MM/dd HH:mm:ss.SSS");
        });
        // 阻塞获取任务结果
        String result = future.get();
        System.out.println(result);

        // shutdown() 方法会停止接受新的任务，等待已提交的任务执行完成后关闭线程池。
        // 这个方法不会强制关闭线程池，如果已经提交的任务没有执行完成，那么它们会继续执行直到完成。
        executorService.shutdown();

        try {
            // 等待所有任务执行完成或超时（这里设置超时时间为1分钟）
            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                // 如果超时仍有任务未完成，则强制关闭ExecutorService
                // shutdownNow() 方法会立即停止所有正在执行的任务，并尝试中断正在执行的任务。
                // 同时，它也会停止接受新的任务，并返回尚未开始执行的任务列表。这个方法会强制关闭线程池，因此可能会导致一些任务未能完成。
                executorService.shutdownNow();
            }
            flag.set(false);
        } catch (InterruptedException e) {
            // 处理中断异常
            e.printStackTrace();
        }
    }

}
