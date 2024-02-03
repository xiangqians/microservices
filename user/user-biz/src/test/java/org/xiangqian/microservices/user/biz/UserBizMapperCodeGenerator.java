package org.xiangqian.microservices.user.biz;

import org.xiangqian.microservices.common.code.generator.MapperCodeGenerator;
import org.xiangqian.microservices.user.model.entity.UserEntity;

/**
 * @author xiangqian
 * @date 19:05 2024/01/29
 */
public class UserBizMapperCodeGenerator {

    public static void main(String[] args) {
        String sql = MapperCodeGenerator.insert(UserEntity.class);
        System.out.println(sql);
    }

}
