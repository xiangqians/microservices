package org.microservices.common.auth.enumeration;

import lombok.Getter;
import org.microservices.common.core.enumeration.Enum;

/**
 * @author xiangqian
 * @date 10:07 2022/03/26
 */
@Getter
public enum Scope implements Enum<String> {
    ALL("all"),
    READ("read"),
    WRITE("write"),
    TRUST("trust"),
    ;

    private final String value;

    Scope(String value) {
        this.value = value;
    }

    @Override
    public String getDescription() {
        return null;
    }

}
