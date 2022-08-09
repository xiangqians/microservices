package org.microservices.common.auth.enumeration;

import lombok.Getter;
import org.microservices.common.core.enumeration.Enum;

/**
 * authorizedGrantTypes授权授予类型
 *
 * @author xiangqian
 * @date 10:06 2022/03/20
 */
@Getter
public enum AuthorizedGrantType implements Enum<String> {
    AUTHORIZATION_CODE("authorization_code"), // 授权码类型
    IMPLICIT("implicit"), // 隐式授权类型
    PASSWORD("password"), // 资源所有者（即用户）密码类型
    CLIENT_CREDENTIALS("client_credentials"), // 客户端凭据（客户端ID以及Key）类型
    REFRESH_TOKEN("refresh_token"), // 通过以上授权获得的刷新令牌来获取新的令牌
    ;
    private final String value;

    AuthorizedGrantType(String value) {
        this.value = value;
    }

    public static Class<?> type() {
        return String.class;
    }

    public static AuthorizedGrantType of(String value) {
        AuthorizedGrantType[] authorizedGrantTypes = AuthorizedGrantType.values();
        for (AuthorizedGrantType authorizedGrantType : authorizedGrantTypes) {
            if (authorizedGrantType.value.equals(value)) {
                return authorizedGrantType;
            }
        }
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
