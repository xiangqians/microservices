package org.microservices.order.biz;

import org.microservices.common.feign.annotation.EnableFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiangqian
 * @date 10:51 2022/04/10
 */
@EnableFeign
@SpringBootApplication
public class OrderBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderBizApplication.class, args);
    }

}
