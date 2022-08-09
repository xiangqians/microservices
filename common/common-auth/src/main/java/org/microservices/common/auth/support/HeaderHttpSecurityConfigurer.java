package org.microservices.common.auth.support;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * @author xiangqian
 * @date 14:47 2022/04/10
 */
public class HeaderHttpSecurityConfigurer implements HttpSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .and()
                .headers().addHeaderWriter((request, response) -> {
                    response.addHeader("Access-Control-Allow-Origin", "*"); // 允许跨域
                    if (request.getMethod().equals("OPTIONS")) { // 如果是跨域的预检请求，则原封不动向下传达请求头信息
                        response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
                        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
                    }
                });
    }

}
