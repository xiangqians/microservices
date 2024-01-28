package org.xiangqian.microservices.common.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author xiangqian
 * @date 20:34 2023/10/18
 */
public class Assert {

    public static void isTrue(boolean expression, String msg) {
        if (!expression) {
            throw new Exception(msg);
        }
    }

    public static void isTrue(boolean expression, Supplier<String> msgSupplier) {
        if (!expression) {
            throw new Exception(msgSupplier);
        }
    }

    public static void isFalse(boolean expression, String msg) {
        if (expression) {
            throw new Exception(msg);
        }
    }

    public static void isFalse(boolean expression, Supplier<String> msgSupplier) {
        if (expression) {
            throw new Exception(msgSupplier);
        }
    }

    public static void isNull(Object object, String msg) {
        if (object != null) {
            throw new Exception(msg);
        }
    }

    public static void isNull(Object object, Supplier<String> msgSupplier) {
        if (object != null) {
            throw new Exception(msgSupplier);
        }
    }

    public static void notNull(Object object, String msg) {
        if (object == null) {
            throw new Exception(msg);
        }
    }

    public static void notNull(Object object, Supplier<String> msgSupplier) {
        if (object == null) {
            throw new Exception(msgSupplier);
        }
    }

    public static void isEmpty(Object[] array, String msg) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new Exception(msg);
        }
    }

    public static void isEmpty(Object[] array, Supplier<String> msgSupplier) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new Exception(msgSupplier);
        }
    }

    public static void isEmpty(Collection<?> collection, String msg) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new Exception(msg);
        }
    }

    public static void isEmpty(Collection<?> collection, Supplier<String> msgSupplier) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new Exception(msgSupplier);
        }
    }

    public static void isEmpty(Map<?, ?> map, String msg) {
        if (MapUtils.isNotEmpty(map)) {
            throw new Exception(msg);
        }
    }

    public static void isEmpty(Map<?, ?> map, Supplier<String> msgSupplier) {
        if (MapUtils.isNotEmpty(map)) {
            throw new Exception(msgSupplier);
        }
    }

    public static void notEmpty(Object[] array, String msg) {
        if (ArrayUtils.isEmpty(array)) {
            throw new Exception(msg);
        }
    }

    public static void notEmpty(Object[] array, Supplier<String> msgSupplier) {
        if (ArrayUtils.isEmpty(array)) {
            throw new Exception(msgSupplier);
        }
    }

    public static void notEmpty(Collection<?> collection, String msg) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new Exception(msg);
        }
    }

    public static void notEmpty(Collection<?> collection, Supplier<String> msgSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new Exception(msgSupplier);
        }
    }

    public static void notEmpty(Map<?, ?> map, String msg) {
        if (MapUtils.isEmpty(map)) {
            throw new Exception(msg);
        }
    }

    public static void notEmpty(Map<?, ?> map, Supplier<String> msgSupplier) {
        if (MapUtils.isEmpty(map)) {
            throw new Exception(msgSupplier);
        }
    }

    public static class Exception extends RuntimeException {
        public Exception(String msg) {
            super(msg);
        }

        public Exception(Supplier<String> msgSupplier) {
            this(msgSupplier != null ? msgSupplier.get() : null);
        }
    }

}
