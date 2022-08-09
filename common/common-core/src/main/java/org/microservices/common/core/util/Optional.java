package org.microservices.common.core.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiangqian
 * @date 00:02 2022/06/12
 */
public class Optional {

    public static <T> java.util.Optional<T> ofNullable(T value) {
        if (Objects.isNull(value)) {
            return java.util.Optional.empty();
        }

        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) value;
            if (CollectionUtils.isEmpty(collection)) {
                return java.util.Optional.empty();
            }

        } else if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            if (MapUtils.isEmpty(map)) {
                return java.util.Optional.empty();
            }
        }

        return java.util.Optional.of(value);
    }

}
