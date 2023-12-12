package org.xiangqian.microservices.common.util;

import com.google.common.base.CaseFormat;

/**
 * 命名转换工具
 * <p>
 * 驼峰式命名法
 * 驼峰式命名法，又叫小驼峰式命名法。要求第一个单词首字母小写，后面其他单词首字母大写。
 * eg:
 * float manHeight;
 * <p>
 * 帕斯卡命名法
 * 帕斯卡命名法，又叫大驼峰式命名法。与小驼峰式命名法的最大区别在于，每个单词的第一个字母都要大写。
 * eg:
 * float ManHeight;
 * <p>
 * 下划线命名法
 * 通过下划线来分割全部都是大写的单词。下划线命名法在宏定义和常量中使用比较多。
 * eg:
 * float man_height;
 *
 * @author xiangqian
 * @date 20:32 2023/10/19
 */
public class NamingConvUtil {

    /**
     * 驼峰式命名（小驼峰式命名）转帕斯卡命名（大驼峰式命名）
     *
     * @param lowerCamel 驼峰式命名（小驼峰式命名）
     * @return
     */
    public static String lowerCamelToUpperCamel(String lowerCamel) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, lowerCamel);
    }

    /**
     * 驼峰式命名（小驼峰式命名）转下划线命名
     *
     * @param lowerCamel 驼峰式命名（小驼峰式命名）
     * @return
     */
    public static String lowerCamelToUnderline(String lowerCamel) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, lowerCamel);
    }

    /**
     * 帕斯卡命名（大驼峰式命名）转驼峰式命名（小驼峰式命名）
     *
     * @param upperCamel 帕斯卡命名（大驼峰式命名）
     * @return
     */
    public static String upperCamelToLowerCamel(String upperCamel) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, upperCamel);
    }

    /**
     * 帕斯卡命名（大驼峰式命名）转下划线命名
     *
     * @param upperCamel 帕斯卡命名（大驼峰式命名）
     * @return
     */
    public static String upperCamelToUnderline(String upperCamel) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, upperCamel);
    }


    /**
     * 下划线命名转驼峰式命名（小驼峰式命名）
     *
     * @param underline 下划线命名
     * @return
     */
    public static String underlineToLowerCamel(String underline) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underline);
    }

    /**
     * 下划线命名转帕斯卡命名（大驼峰式命名）
     *
     * @param underline 下划线命名
     * @return
     */
    public static String underlineToUpperCamel(String underline) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, underline);
    }

}
