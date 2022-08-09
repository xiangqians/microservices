package org.microservices.common.lock.provider;

/**
 * @author xiangqian
 * @date 22:58 2022/07/20
 */
public interface Lock {

    void lock() throws InterruptedException;

    void unlock();

}
