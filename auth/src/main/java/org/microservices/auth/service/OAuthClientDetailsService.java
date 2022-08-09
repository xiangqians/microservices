package org.microservices.auth.service;

import org.microservices.auth.dto.OAuthClientDetailsDto;

/**
 * @author xiangqian
 * @date 00:58 2022/06/24
 */
public interface OAuthClientDetailsService {

    OAuthClientDetailsDto queryByClientId(String clientId);

}
