package org.microservices.common.core.o;

/**
 * DTO Parameter
 *
 * @author xiangqian
 * @date 18:50 2022/06/11
 */
public interface DtoParam extends O {

    default <T extends PoParam> T convertToPoParam(Class<T> type) {
        return convertToPoParam(type, null);
    }

    /**
     * {@link DtoParam} 转为 {@link PoParam}
     *
     * @param type
     * @param additionalInfo
     * @param <T>
     * @return
     */
    default <T extends PoParam> T convertToPoParam(Class<T> type, String additionalInfo) {
        throw new UnsupportedOperationException();
    }

    default <T extends BoParam> T convertToBoParam(Class<T> type) {
        return convertToBoParam(type, null);
    }

    /**
     * {@link DtoParam} 转为 {@link BoParam}
     *
     * @param type
     * @param additionalInfo
     * @param <T>
     * @return
     */
    default <T extends BoParam> T convertToBoParam(Class<T> type, String additionalInfo) {
        throw new UnsupportedOperationException();
    }

    /**
     * 处理参数
     */
    default void post() {
    }

}
