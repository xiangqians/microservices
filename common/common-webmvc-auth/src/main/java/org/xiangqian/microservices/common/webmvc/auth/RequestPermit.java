package org.xiangqian.microservices.common.webmvc.auth;

import java.lang.annotation.*;

/**
 * 允许未经授权的请求
 *
 * @author xiangqian
 * @date 19:30 2023/07/31
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestPermit {

    // 是否仅仅是内部调用
    boolean inner() default false;

}
