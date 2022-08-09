package org.microservices.common.db.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author xiangqian
 * @date 15:37 2022/03/26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Dao {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
