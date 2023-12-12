package org.xiangqian.microservices.common.auth.resource;

import java.lang.annotation.*;

/**
 * 允许未经授权的请求
 *Request permit
 *
 * @author xiangqian
 * @date 19:30 2023/07/31
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestPermit {
}
