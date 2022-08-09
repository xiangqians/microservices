package org.microservices.common.lock.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.microservices.common.lock.enumeration.LockType;

import java.util.concurrent.TimeUnit;

/**
 * 锁配置信息
 *
 * @author xiangqian
 * @date 17:27 2022/04/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockProperties implements Cloneable {

    /**
     * 锁名称
     */
    private String lockName;

    /**
     * 锁类型
     */
    private LockType lockType;

    /**
     * 尝试获取锁等待时间
     */
    private long waitTime;

    /**
     * 租期，-1表示永久
     */
    private long leaseTime;

    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 锁的提供者
     */
    private String lockProvider;

    @Override
    public LockProperties clone() {
        LockProperties lockProperties = new LockProperties();
        lockProperties.setLockName(getLockName());
        lockProperties.setLockType(getLockType());
        lockProperties.setWaitTime(getWaitTime());
        lockProperties.setLeaseTime(getLeaseTime());
        lockProperties.setTimeUnit(getTimeUnit());
        lockProperties.setLockProvider(getLockProvider());
        return lockProperties;
    }

}
