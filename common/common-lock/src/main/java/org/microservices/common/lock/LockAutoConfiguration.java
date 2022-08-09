package org.microservices.common.lock;

import org.microservices.common.lock.provider.LockProvider;
import org.microservices.common.lock.provider.ReentrantLockProvider;
import org.microservices.common.lock.support.LockSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiangqian
 * @date 20:49 2022/06/18
 */
@Configuration
public class LockAutoConfiguration {

    @Bean
    public LockSupport lockableSupport() {
        return new LockSupport();
    }

    @Bean
    @ConditionalOnMissingBean(name = "defaultLockProvider")
    public LockProvider defaultLockProvider() {
        return new ReentrantLockProvider();
    }

}
