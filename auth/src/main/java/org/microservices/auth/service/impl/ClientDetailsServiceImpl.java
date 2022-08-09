package org.microservices.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.microservices.auth.dao.OAuthClientDetailsDao;
import org.microservices.auth.dto.OAuthClientDetailsDto;
import org.microservices.auth.service.OAuthClientDetailsService;
import org.microservices.common.core.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import java.util.Objects;

/**
 * 客户端详情服务
 * <p>
 * implements {@link ClientDetailsService}
 * <p>
 * security提供 {@link ClientDetailsService} 实现类有：
 * 1、{@link InMemoryClientDetailsService} 将ClientDetails存储在内存
 * 2、{@link JdbcClientDetailsService} 将ClientDetails存储在数据库
 *
 * @author xiangqian
 * @date 20:31 2022/04/03
 */
@Slf4j
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    private OAuthClientDetailsService oauthClientDetailsService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.info("loadClient: {}", clientId);
        ClientDetails clientDetails = oauthClientDetailsService.queryByClientId(clientId);
        if (Objects.isNull(clientDetails)) {
            throw new NoSuchClientException(String.format("No client with requested id: %s", clientId));
        }
        log.info("clientDetails: {}", clientDetails);
        return clientDetails;
    }

}
