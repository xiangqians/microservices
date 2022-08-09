package org.microservices.common.feign.annotation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author xiangqian
 * @date 21:34:40 2022/03/27
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FeignClient
public @interface FeignService {

    @AliasFor(annotation = FeignClient.class)
    String value() default "";

    @AliasFor(annotation = FeignClient.class)
    String contextId() default "";

    @AliasFor(annotation = FeignClient.class)
    String name() default "";

    @AliasFor(annotation = FeignClient.class)
    String[] qualifiers() default {};

    @AliasFor(annotation = FeignClient.class)
    String url() default "";

    @AliasFor(annotation = FeignClient.class)
    boolean decode404() default false;

    @AliasFor(annotation = FeignClient.class)
    Class<?>[] configuration() default {};

    @AliasFor(annotation = FeignClient.class)
    Class<?> fallback() default void.class;

    @AliasFor(annotation = FeignClient.class)
    Class<?> fallbackFactory() default void.class;

    @AliasFor(annotation = FeignClient.class)
    String path() default "";

    @AliasFor(annotation = FeignClient.class)
    boolean primary() default true;

}
