package org.microservices.common.core.o;

/**
 * DTO Parameter
 *
 * @author xiangqian
 * @date 18:50 2022/06/11
 */
public interface Dtpo extends O {

    default <T extends Ppo> T convertToPpo(Class<T> type) {
        throw new UnsupportedOperationException();
    }

    default <T extends Bpo> T convertToBpo(Class<T> type) {
        throw new UnsupportedOperationException();
    }

    /**
     * 处理参数
     */
    default void post() {
    }

}
