package org.microservices.auth.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import org.microservices.auth.dto.OAuthClientDetailsDto;
import org.microservices.common.core.o.Dto;
import org.microservices.common.core.o.Po;
import org.microservices.common.core.util.JacksonUtils;
import org.microservices.common.core.util.Optional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 客户端详情
 *
 * @author xiangqian
 * @date 22:31 2022/06/23
 */
@Data
@TableName("oauth_client_details")
public class OAuthClientDetailsPo implements Po {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端id
     */
    @TableId(type = IdType.INPUT)
    private String clientId;

    /**
     * 资源ids
     */
    private String resourceIds;

    /**
     * 密钥是否必须，0-不必须；1-必须
     */
    private String secretRequired;

    /**
     * 客户端密码，client_secret字段不能直接是 secret 的原始值，需要经过加密
     */
    private String clientSecret;

    /**
     * 该客户端允许授权的范围，定义客户端的权限，这里只是一个标识，资源服务可以根据这个权限进行鉴权
     */
    @TableField("`scope`")
    private String scope;

    /**
     * 该客户端允许授权的类型
     */
    private String authorizedGrantTypes;

    /**
     * 跳转的uri
     */
    private String registeredRedirectUris;

    /**
     * authorities
     */
    private String authorities;

    /**
     * 访问令牌有效期，单位：s
     * 令牌默认有效期2小时（60 * 60 * 2 s）
     */
    private Integer accessTokenValidity;

    /**
     * 刷新令牌有效期，单位：s
     * 刷新令牌默认有效期3天（60 * 60 * 24 * 3 s）
     */
    private Integer refreshTokenValidity;

    /**
     * 0-跳转到授权页面；1-不跳转，直接发令牌
     */
    private String autoApprove;

    /**
     * 附加信息
     */
    private String additionalInformation;

    /**
     * 删除标识，0-正常，1-删除
     */
    @TableLogic
    private String delFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


    @Override
    public <T extends Dto> T convertToDto(Class<T> type) {

        if (type == OAuthClientDetailsDto.class) {
            try {
                return (T) convertToOAuthClientDetailsDto();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return Po.super.convertToDto(type);
    }

    private OAuthClientDetailsDto convertToOAuthClientDetailsDto() throws IOException {
        OAuthClientDetailsDto dto = new OAuthClientDetailsDto();
        dto.setClientId(getClientId());
        dto.setResourceIds(JacksonUtils.toObject(getResourceIds(), new TypeReference<Set<String>>() {
        }));
        dto.setSecretRequired("1".equals(getSecretRequired()) ? true : false);
        dto.setClientSecret(getClientSecret());
        dto.setScope(JacksonUtils.toObject(getScope(), new TypeReference<Set<String>>() {
        }));
        dto.setAuthorizedGrantTypes(JacksonUtils.toObject(getAuthorizedGrantTypes(), new TypeReference<Set<String>>() {
        }));
        dto.setRegisteredRedirectUris(JacksonUtils.toObject(getRegisteredRedirectUris(), new TypeReference<Set<String>>() {
        }));
        dto.setAuthorities(Collections.emptyList());
        dto.setAccessTokenValidity(getAccessTokenValidity());
        dto.setRefreshTokenValidity(getRefreshTokenValidity());
        dto.setAutoApprove("1".equals(getAutoApprove()) ? true : false);
        dto.setAdditionalInformation(Optional.ofNullable(getAdditionalInformation())
                .map(additionalInformation -> {
                    try {
                        return JacksonUtils.toObject(getRegisteredRedirectUris(), new TypeReference<Map<String, Object>>() {
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(null));
        dto.setCreateTime(getCreateTime());
        dto.setUpdateTime(getUpdateTime());
        return dto;
    }

}
