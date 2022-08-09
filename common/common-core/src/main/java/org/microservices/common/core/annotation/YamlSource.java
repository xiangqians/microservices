package org.microservices.common.core.annotation;

import org.microservices.common.core.support.YamlPropertySourceFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.io.support.PropertySourceFactory;

import java.lang.annotation.*;

/**
 * @author xiangqian
 * @date 22:23 2022/04/04
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PropertySource({})
public @interface YamlSource {

    @AliasFor(annotation = PropertySource.class)
    String name() default "";

    @AliasFor(annotation = PropertySource.class)
    String[] value();

    @AliasFor(annotation = PropertySource.class)
    boolean ignoreResourceNotFound() default false;

    @AliasFor(annotation = PropertySource.class)
    String encoding() default "";

    @AliasFor(annotation = PropertySource.class)
    Class<? extends PropertySourceFactory> factory() default YamlPropertySourceFactory.class;

}
