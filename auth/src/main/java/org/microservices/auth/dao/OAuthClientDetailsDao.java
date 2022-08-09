package org.microservices.auth.dao;

import org.microservices.auth.po.OAuthClientDetailsPo;

/**
 * @author xiangqian
 * @date 22:37 2022/06/23
 */
public interface OAuthClientDetailsDao {

    OAuthClientDetailsPo queryByClientId(String clientId);

}
