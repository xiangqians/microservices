package org.xiangqian.microservices.common.model;

import java.lang.annotation.*;

/**
 * @author xiangqian
 * @date 20:00 2023/11/10
 */
public interface Code {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Description {
        /**
         * 中文描述
         *
         * @return
         */
        String zh() default "";

        /**
         * 英文描述
         *
         * @return
         */
        String en() default "";
    }

}
