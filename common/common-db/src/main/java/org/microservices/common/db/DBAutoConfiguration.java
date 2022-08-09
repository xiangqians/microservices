package org.microservices.common.db;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author xiangqian
 * @date 11:59 2022/04/02
 */
@Configuration
@EnableTransactionManagement // 开启事务管理
//@MapperScan({"org.microservices.**.mapper"}) // 不使用MapperScan扫描Mapper接口，Mapper接口需要使用@Mapper注解
public class DBAutoConfiguration {
}
