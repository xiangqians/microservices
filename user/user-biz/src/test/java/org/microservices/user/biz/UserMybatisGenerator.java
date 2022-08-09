package org.microservices.user.biz;

import org.junit.Test;
import org.microservices.common.test.db.MybatisGenerator;

/**
 * @author xiangqian
 * @date 10:41:56 2022/06/11
 */
public class UserMybatisGenerator {

    @Test
    public void demo() {
        MybatisGenerator mybatisGenerator = new MybatisGenerator.Builder()
                .author("xiangqian")

                // db config
                .dbConfig()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://mysql:3306/user?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true")
                .username("microservices")
                .password("microservices")
                .and()

                // output config
                .outputConfig()
                .moduleName("user.biz")
                .tables("user", "role", "user_role", "permission", "role_permission")
                .outputDir(null)
                .and()

                // build
                .build();

        mybatisGenerator.execute();
    }

}
