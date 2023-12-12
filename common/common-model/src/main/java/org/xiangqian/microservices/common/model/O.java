package org.xiangqian.microservices.common.model;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * object
 *
 * @author xiangqian
 * @date 20:16 2023/05/05
 */
public interface O extends Serializable {

    @SneakyThrows
    default <T> T copyProperties(Class<T> type) {
        T t = type.getConstructor().newInstance();
        BeanUtils.copyProperties(this, t);
        return t;
    }

}
