package org.microservices.auth.dto;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.microservices.common.core.o.Dto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author xiangqian
 * @date 22:49 2022/06/23
 */
@Data
public class OAuthClientDetailsDto implements Dto, ClientDetails {

    private String clientId;
    private Set<String> resourceIds;
    private Boolean secretRequired;
    private String clientSecret;
    private Set<String> scope;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUris;
    private Collection<GrantedAuthority> authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private Boolean autoApprove;
    private Map<String, Object> additionalInformation;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Override
    public boolean isSecretRequired() {
        return secretRequired;
    }

    @Override
    public boolean isScoped() {
        return CollectionUtils.isNotEmpty(getScope());
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUris;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValidity;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValidity;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return autoApprove;
    }

}
