package org.xiangqian.microservices.order.biz;

import org.xiangqian.microservices.common.code.generator.MyBatisCodeGenerator;

/**
 * @author xiangqian
 * @date 21:11 2023/10/16
 */
public class OrderBizMyBatisCodeGenerator {

    public static void main(String[] args) {
        MyBatisCodeGenerator.builder()
                .author("xiangqian")
                .tables("order")
                .build()
                .execute();
    }

}
