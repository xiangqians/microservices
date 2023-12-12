package org.xiangqian.microservices.common.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * code码工具
 *
 * @author xiangqian
 * @date 20:00 2023/11/10
 */
public class CodeUtil {

    // <code, 描述>
    private static Map<String, String> cache;

    static {
        init();
    }

    @SneakyThrows
    private static void init() {
        cache = new HashMap<>(1024, 1f);
        Set<Class<?>> classes = ResourceUtil.scanClasses("org.xiangqian.microservices.**.model");
        for (Class<?> clazz : classes) {
            if (clazz.isInterface() && clazz.getSimpleName().endsWith("Code")) {
                Field[] fields = clazz.getFields();
                for (Field field : fields) {
                    if (field.getType() == String.class && field.isAnnotationPresent(Description.class)) {
                        Description description = field.getAnnotation(Description.class);
                        cache.put((String) field.get(null), description.value());
                    }
                }
            }
        }
    }

    public static String getDescription(String code) {
        return cache.get(code);
    }

}
