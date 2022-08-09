package org.microservices.common.auth.annotation;

import java.lang.annotation.*;

/**
 * 允许未经授权的请求
 *
 * @author xiangqian
 * @date 20:30 2022/04/02
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AllowUnauthorizedRequest {
}
