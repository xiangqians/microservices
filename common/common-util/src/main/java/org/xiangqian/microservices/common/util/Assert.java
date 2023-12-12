package org.xiangqian.microservices.common.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author xiangqian
 * @date 20:34 2023/10/18
 */
public class Assert {

    public static void isTrue(boolean expression, String code) {
        if (!expression) {
            throw new Exception(code);
        }
    }

    public static void isNull(Object object, String code) {
        if (object != null) {
            throw new Exception(code);
        }
    }

    public static void notNull(Object object, String code) {
        if (object == null) {
            throw new Exception(code);
        }
    }

    public static void isEmpty(Object[] array, String code) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new Exception(code);
        }
    }

    public static void isEmpty(Collection<?> collection, String code) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new Exception(code);
        }
    }

    public static void isEmpty(Map<?, ?> map, String code) {
        if (MapUtils.isNotEmpty(map)) {
            throw new Exception(code);
        }
    }

    public static void notEmpty(Object[] array, String code) {
        if (ArrayUtils.isEmpty(array)) {
            throw new Exception(code);
        }
    }

    public static void notEmpty(Collection<?> collection, String code) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new Exception(code);
        }
    }

    public static void notEmpty(Map<?, ?> map, String code) {
        if (MapUtils.isEmpty(map)) {
            throw new Exception(code);
        }
    }

    public static class Exception extends RuntimeException {
        public Exception(String code) {
            super(code);
        }
    }

}
