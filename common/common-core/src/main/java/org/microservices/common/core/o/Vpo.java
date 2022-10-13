package org.microservices.common.core.o;

import org.microservices.common.core.util.BeanConvertUtils;

/**
 * VO Parameter
 *
 * @author xiangqian
 * @date 18:04 2022/06/11
 */
public interface Vpo extends O {

    default <T extends Bpo> T convertToBpo(Class<T> type) {
        throw new UnsupportedOperationException();
    }

    default <T extends Ppo> T convertToPpo(Class<T> type) {
        try {
            return BeanConvertUtils.convert(this, type);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    default <T extends Dtpo> T convertToDtpo(Class<T> type) {
        throw new UnsupportedOperationException();
    }

    /**
     * 处理参数
     */
    default void post() {
    }

}
