package org.microservices.auth.service.impl;

import org.microservices.auth.dao.OAuthClientDetailsDao;
import org.microservices.auth.dto.OAuthClientDetailsDto;
import org.microservices.auth.service.OAuthClientDetailsService;
import org.microservices.common.core.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xiangqian
 * @date 00:59 2022/06/24
 */
@Service
public class OAuthClientDetailsServiceImpl implements OAuthClientDetailsService {

    @Autowired
    private OAuthClientDetailsDao oauthClientDetailsDao;

    @Transactional(readOnly = true, timeout = 5)
    @Override
    public OAuthClientDetailsDto queryByClientId(String clientId) {
        return Optional.ofNullable(oauthClientDetailsDao.queryByClientId(clientId))
                .map(clientDetailsPo -> clientDetailsPo.convertToDto(OAuthClientDetailsDto.class))
                .orElse(null);
    }

}
