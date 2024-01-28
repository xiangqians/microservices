package org.xiangqian.microservices.common.codegen;

import java.lang.reflect.Field;

/**
 * @author xiangqian
 * @date 19:41 2024/01/09
 */
public class TrimSetterGenerator {

    public final static void execute(Class<?> clazz) {
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
