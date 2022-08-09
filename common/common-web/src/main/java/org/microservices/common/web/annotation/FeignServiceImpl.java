package org.microservices.common.web.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * @author xiangqian
 * @date 21:37 2022/03/27
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface FeignServiceImpl {

    @AliasFor(annotation = Controller.class)
    String value() default "";

}
