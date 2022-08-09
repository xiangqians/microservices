package org.microservices.common.core.o;

/**
 * PO Parameter
 *
 * @author xiangqian
 * @date 18:49 2022/06/11
 */
public interface PoParam extends O {

    default <T extends PoParam> T of(DtoParam dtoParam) {
        throw new UnsupportedOperationException();
    }

    default <T extends PoParam> T of(VoParam dtoParam) {
        throw new UnsupportedOperationException();
    }

}
