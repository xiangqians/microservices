package org.microservices.auth.dao.impl;

import org.microservices.auth.dao.OAuthClientDetailsDao;
import org.microservices.auth.mapper.OAuthClientDetailsMapper;
import org.microservices.auth.po.OAuthClientDetailsPo;
import org.microservices.common.db.annotation.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author xiangqian
 * @date 22:38 2022/06/23
 */
@Dao
public class OAuthClientDetailsDaoImpl implements OAuthClientDetailsDao {

    public static final String CACHE_NAME = "CACHE_OAUTH_CLIENT_DETAILS_DAO";

    @Autowired
    private OAuthClientDetailsMapper oauthClientDetailsMapper;

    @Cacheable(cacheNames = CACHE_NAME, key = "'clientId_'+#clientId", unless = "#result == null")
    @Override
    public OAuthClientDetailsPo queryByClientId(String clientId) {
        return oauthClientDetailsMapper.selectById(clientId);
    }

}
