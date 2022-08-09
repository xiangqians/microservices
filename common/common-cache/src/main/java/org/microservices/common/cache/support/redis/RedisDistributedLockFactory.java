//package org.microservices.common.cache.support;
//
//import lombok.SneakyThrows;
//import org.microservices.common.core.locks.LockFactory;
//import org.microservices.common.core.locks.LockProperties;
//import org.microservices.common.core.locks.LockedException;
//import org.redisson.api.LockOptions;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//
///**
// * @author xiangqian
// * @date 17:59 2022/04/17
// */
//public class RedisDistributedLockFactory implements LockFactory {
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    @Override
//    public Lock getLock(LockProperties properties) {
//        switch (properties.getLockType()) {
//            case FAIR_LOCK:
//                return createFairLock(redissonClient, properties.getName());
//
//            case UNFAIR_LOCK:
//                return createUnfairLock(redissonClient, properties.getName());
//
//            case SPIN_LOCK:
//                return createSpinLock(redissonClient, properties.getName());
//
//            default:
//                return createFairLock(redissonClient, properties.getName());
//        }
//    }
//
//    /**
//     * 创建默认锁（非公平锁）
//     *
//     * @param redissonClient redis client
//     * @param name           锁名称
//     * @return
//     */
//    public static RedisDistributedLock createUnfairLock(RedissonClient redissonClient, String name) {
//        return new RedisDistributedLock(redissonClient.getLock(name));
//    }
//
//    /**
//     * 创建公平锁
//     *
//     * @param redissonClient redis client
//     * @param name           锁名称
//     * @return
//     */
//    public static RedisDistributedLock createFairLock(RedissonClient redissonClient, String name) {
//        return new RedisDistributedLock(redissonClient.getFairLock(name));
//    }
//
//    /**
//     * 创建自旋锁
//     *
//     * @param redissonClient
//     * @param name
//     * @return
//     */
//    public static RedisDistributedLock createSpinLock(RedissonClient redissonClient, String name) {
//        return new RedisDistributedLock(redissonClient.getSpinLock(name));
//    }
//
//    /**
//     * @param redissonClient
//     * @param name
//     * @param backOff
//     * @return
//     */
//    public static RedisDistributedLock createSpinLock(RedissonClient redissonClient, String name, LockOptions.BackOff backOff) {
//        return new RedisDistributedLock(redissonClient.getSpinLock(name, backOff));
//    }
//
//    public static class RedisDistributedLock implements Lock {
//
//        private RLock lock;
//
//        public RedisDistributedLock(RLock lock) {
//            this.lock = lock;
//
//            // 判断当前锁是否被加锁了
//            if (lock.isLocked()) {
//                throw new LockedException();
//            }
//        }
//
//        @Override
//        public void lock() {
//            // 加锁。需要 {@link java.util.concurrent.locks.Lock#unlock()} 释放锁，否则可能会导致死锁
////        lock.lock();
//
//            // 加锁。锁是有租期的，超过时间则会自动释放锁
//            long leaseTime = 8;
//            TimeUnit unit = TimeUnit.MINUTES;
//            lock.lock(leaseTime, unit);
//        }
//
//        @Override
//        public void lockInterruptibly() throws InterruptedException {
//            throw new UnsupportedOperationException();
//        }
//
//        @SneakyThrows
//        @Override
//        public boolean tryLock() {
//            long waitTime = 1; // 尝试获取锁等待时间
//            long leaseTime = -1; // 租期
//            TimeUnit unit = TimeUnit.MINUTES;
//            return lock.tryLock(waitTime, leaseTime, TimeUnit.MINUTES);
//        }
//
//        @Override
//        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
//            return lock.tryLock(time, TimeUnit.MINUTES);
//        }
//
//        @Override
//        public void unlock() {
//            // 当前锁是否由当前线程持有
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//
//        @Override
//        public Condition newCondition() {
//            throw new UnsupportedOperationException();
//        }
//    }
//
//}
