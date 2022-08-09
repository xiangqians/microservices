package org.microservices.common.feign.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 开启使用Feign客户端调用。扫描@FeignClient标注的FeignClient接口
 *
 * @author xiangqian
 * @date 13:09:53 2022/03/26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableFeign {

    @AliasFor(annotation = EnableFeignClients.class)
    String[] basePackages() default {"org.microservices.**.feign.service"};

}
