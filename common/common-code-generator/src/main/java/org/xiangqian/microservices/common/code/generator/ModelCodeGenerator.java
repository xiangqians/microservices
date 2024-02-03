package org.xiangqian.microservices.common.code.generator;

import java.lang.reflect.Field;

/**
 * @author xiangqian
 * @date 19:59 2024/01/29
 */
public class ModelCodeGenerator {

    public static void trimSetter(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                String name = field.getName();
                System.out.format("public void set%s(String %s) {", String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1), name).println();
                System.out.format("\tthis.%s = StringUtils.trim(%s);", name, name).println();
                System.out.format("}").println();
            }
        }
    }

}
