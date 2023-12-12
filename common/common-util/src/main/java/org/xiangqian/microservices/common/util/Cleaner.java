package org.xiangqian.microservices.common.util;

/**
 * Cleaner
 *
 * @author xiangqian
 * @date 19:56 2023/07/27
 */
public class Cleaner implements AutoCloseable { // 实现AutoCloseable接口

    // 创建一个回收对象
    private static final java.lang.ref.Cleaner cleaner = java.lang.ref.Cleaner.create();

    private java.lang.ref.Cleaner.Cleanable cleanable;

    private Cleaner(Runnable action) {
        // 注册一个回收线程
        cleanable = cleaner.register(this, action);
    }

    @Override
    public void close() throws Exception {
        // 释放时进行清除
        cleanable.clean();
    }

    public static void add(Cleanable cleanable) {
        new Cleaner(() ->  // 设计有一个回收线程
                // Cleaner触发清除
                cleanable.clean());
    }

}
