package org.microservices.common.lock.provider;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.microservices.common.lock.enumeration.LockType;
import org.microservices.common.lock.properties.LockProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiangqian
 * @date 22:59 2022/06/18
 */
public class ReentrantLockProvider implements LockProvider {

    /**
     * 缓存 {@link ReentrantLock}
     */
    private Cache cache;

    @PostConstruct
    public void init() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 初始缓存容量
                .initialCapacity(1024)
                // 设置写后过期时间
                .expireAfterWrite(Duration.ofMinutes(10))
                // 设置cache的最大缓存数量
                .maximumSize(1024 * 1024));

        // 设置缓存加载器
        cacheManager.setCacheLoader(key -> null);

        // 不允许空值
        cacheManager.setAllowNullValues(false);

        // 获取cache
        cache = cacheManager.getCache("default");
    }

    @Override
    public Lock getLock(LockProperties lockProperties) {
        String lockName = lockProperties.getLockName();
        Lock lock = cache.get(lockName, Lock.class);
        if (Objects.isNull(lock)) {
            synchronized (cache) {
                lock = cache.get(lockName, Lock.class);
                if (Objects.isNull(lock)) {
                    lock = new LockImpl(lockProperties);
                    cache.put(lockName, lock);
                }
            }
        }
        return lock;
    }

    public static class LockImpl implements Lock {
        private LockProperties lockProperties;
        private ReentrantLock reentrantLock;

        public LockImpl(LockProperties lockProperties) {
            this.lockProperties = lockProperties;
            boolean fair = lockProperties.getLockType() == LockType.FAIR_LOCK;
            this.reentrantLock = new ReentrantLock(fair);
        }

        @Override
        public void lock() throws InterruptedException {
            reentrantLock.tryLock(lockProperties.getWaitTime(), lockProperties.getTimeUnit());
        }

        @Override
        public void unlock() {
            reentrantLock.unlock();
        }
    }

}
