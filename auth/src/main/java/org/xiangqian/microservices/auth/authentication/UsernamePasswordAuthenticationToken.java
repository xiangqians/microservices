package org.xiangqian.microservices.auth.authentication;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 密码模式-认证凭证
 *
 * @author xiangqian
 * @date 19:48 2023/07/21
 */
@Getter
public class UsernamePasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private Set<String> scopes;

    public UsernamePasswordAuthenticationToken(Set<String> scopes, Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(AuthorizationGrantType.PASSWORD, clientPrincipal, additionalParameters);
        this.scopes = Optional.ofNullable(scopes)
                .filter(CollectionUtils::isNotEmpty)
                .map(Collections::unmodifiableSet)
                .orElse(Collections.emptySet());
    }

}
