package org.microservices.common.lock.enumeration;

/**
 * 锁类型
 *
 * @author xiangqian
 * @date 22:03 2022/04/16
 */
public enum LockType {

    FAIR_LOCK, // 公平锁
    UNFAIR_LOCK, // 非公平锁
    SPIN_LOCK, // 自旋锁

}
