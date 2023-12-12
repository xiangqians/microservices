package org.xiangqian.microservices.order.biz;

import org.xiangqian.microservices.common.codegen.CodeGenerator;

/**
 * @author xiangqian
 * @date 21:11 2023/10/16
 */
public class OrderBizCodeGenerator {

    public static void main(String[] args) {
        CodeGenerator.builder()
                .author("xiangqian")
                .tables("order")
                .build()
                .execute();
    }

}
