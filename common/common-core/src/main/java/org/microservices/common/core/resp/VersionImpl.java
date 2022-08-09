package org.microservices.common.core.resp;

import lombok.Getter;

/**
 * @author xiangqian
 * @date 19:53 2022/06/18
 */
@Getter
public enum VersionImpl implements Version {

    V202203("V202203"),
    ;

    private final String value;

    VersionImpl(String value) {
        this.value = value;
    }

}
