package org.xiangqian.microservices.user.biz;

import org.xiangqian.microservices.common.codegen.CodeGenerator;
import org.xiangqian.microservices.common.util.CodeUtil;

/**
 * @author xiangqian
 * @date 21:57 2023/09/04
 */
public class UserBizCodeGenerator {

    public static void main(String[] args) {

        String description = CodeUtil.getDescription("phone_not_empty");
        System.out.println(description);
        description = CodeUtil.getDescription("phone_not_empty1");
        System.out.println(description);

        if (true) {
            return;
        }

        CodeGenerator.builder()
                .author("xiangqian")
                .tables("user")
                .build()
                .execute();
    }

}
