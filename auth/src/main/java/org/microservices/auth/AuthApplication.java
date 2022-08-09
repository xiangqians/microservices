package org.microservices.auth;

//import org.microservices.common.feign.annotation.EnableFeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * VM options:
 * -Dserver.port=3001
 *
 * @author xiangqian
 * @date 21:53 2022/03/20
 */
//@EnableFeign
@RestController
@SpringBootApplication
public class AuthApplication {

    @GetMapping("/")
    public long test() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
