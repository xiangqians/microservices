package org.xiangqian.microservices.common.util;

import java.lang.annotation.*;

/**
 * 描述
 *
 * @author xiangqian
 * @date 20:56 2023/11/10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Description {

    String value();

}
