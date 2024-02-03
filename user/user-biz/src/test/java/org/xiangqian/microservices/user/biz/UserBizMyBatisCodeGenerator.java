package org.xiangqian.microservices.user.biz;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
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
/*
@DecimalMin：用于标注数字类型的字段、方法或参数，指示值的最小值（包括边界）。
@DecimalMax：用于标注数字类型的字段、方法或参数，指示值的最大值（包括边界）。

@Pattern：用于标注字段、方法或参数，指示字符串值必须匹配指定的正则表达式。
@Email：用于标注字段、方法或参数，指示字符串值必须是有效的电子邮件地址格式。

@Digits：用于标注数字类型的字段、方法或参数，指示值的整数和小数部分的位数限制。
@Positive：用于标注数字类型的字段、方法或参数，指示值必须为正数。
@Negative：用于标注数字类型的字段、方法或参数，指示值必须为负数。
@Past：用于标注字段、方法或参数，指示日期或时间值必须在当前时间之前。
@Future：用于标注字段、方法或参数，指示日期或时间值必须在当前时间之后。

*/
