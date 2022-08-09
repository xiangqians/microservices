package org.microservices.common.core;

import org.microservices.common.core.annotation.YamlSource;
import org.microservices.common.core.configure.JacksonConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiangqian
 * @date 11:59 2022/04/06
 */
@Configuration
@YamlSource({"classpath:microservices/core.yml"})
@ImportAutoConfiguration({JacksonConfiguration.class})
public class CoreAutoConfiguration {

}
