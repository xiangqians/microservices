package org.xiangqian.microservices.common.lock;

import java.lang.annotation.*;

/**
 * @author xiangqian
 * @date 21:05 2024/02/28
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

    /**
     * 超时时间，单位：ms
     *
     * @return
     */
    int timeout() default -1;

}
