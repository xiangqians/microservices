package org.microservices.common.lock.exception;

/**
 * 当前锁已被锁住异常
 *
 * @author xiangqian
 * @date 21:42 2022/04/16
 */
public class LockedException extends RuntimeException {

    public LockedException() {
    }

    public LockedException(String message) {
        super(message);
    }

    public LockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockedException(Throwable cause) {
        super(cause);
    }

}
