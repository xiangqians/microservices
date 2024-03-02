package org.xiangqian.microservices.user.biz;

import org.xiangqian.microservices.common.code.generator.MyBatisCodeGenerator;

/**
 * @author xiangqian
 * @date 21:57 2023/09/04
 */
public class UserBizMyBatisCodeGenerator {

    public static void main(String[] args) {
        MyBatisCodeGenerator.builder()
                .author("xiangqian")
                .tables("order")
                .build()
                .execute();
    }

}