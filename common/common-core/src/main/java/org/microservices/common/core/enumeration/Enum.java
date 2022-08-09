package org.microservices.common.core.enumeration;

import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiangqian
 * @date 14:01 2022/04/16
 */
public interface Enum<T> {

    @Nonnull
    T getValue();

    String getDescription();

    //static Enum of(T value);
    //static Class<?> type();

    static <T> Enum<T> valueOf(Class<?> type, T value) {
        if (Objects.isNull(value)) {
            return null;
        }

        try {
            return Arrays.stream(Cache.get(type))
                    .filter(e -> e.getValue().toString().equals(value.toString()))
                    .findFirst()
                    .orElse(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    class Cache {
        private static final Object LOCK = new Object();
        private static final Map<Class<?>, Enum[]> MAP = new ConcurrentHashMap<>();

        static Enum[] get(Class<?> type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Assert.notNull(type, "枚举类型不能为null");
            Assert.isTrue(type.isEnum(), String.format("%s 该类型不是一个枚举类型", type));
            Assert.isTrue(Enum.class.isAssignableFrom(type), String.format("%s 该类型并未实现 %s 接口", type, Enum.class.getName()));

            Enum[] enums = MAP.get(type);
            if (Objects.isNull(enums)) {
                synchronized (LOCK) {
                    if (Objects.isNull(enums = MAP.get(type))) {
                        enums = (Enum[]) type.getMethod("values").invoke(null);
                        MAP.put(type, enums);
                    }
                }
            }
            return enums;
        }

    }

}
