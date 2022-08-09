package org.microservices.common.auth.support;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * {@link HttpSecurity} 配置器
 *
 * @author xiangqian
 * @date 23:43 2022/07/06
 */
public interface HttpSecurityConfigurer {

    void configure(HttpSecurity http) throws Exception;

}
