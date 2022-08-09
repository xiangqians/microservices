package org.microservices.common.lock.provider;

import org.microservices.common.lock.properties.LockProperties;

/**
 * 锁提供者
 * <p>
 * synchronized, ReentrantLock, redis, zookeeper
 *
 * @author xiangqian
 * @date 22:26 2022/06/18
 */
public interface LockProvider {

    Lock getLock(LockProperties lockProperties);

}
