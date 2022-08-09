package org.microservices.user.biz.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.microservices.common.core.pagination.Page;
import org.microservices.common.core.pagination.PageRequest;
import org.microservices.common.db.annotation.Dao;
import org.microservices.common.db.support.DaoHelper;
import org.microservices.user.biz.dao.UserDao;
import org.microservices.user.biz.mapper.UserMapper;
import org.microservices.user.biz.po.UserPo;
import org.microservices.user.biz.po.param.UserPoParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Objects;

/**
 * @author xiangqian
 * @date 09:48 2022/04/03
 */
@Dao
public class UserDaoImpl implements UserDao {

    public static final String CACHE_NAME = "CACHE_USER_DAO";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<UserPo> queryForPage(PageRequest pageRequest, UserPoParam poParam) {
        LambdaQueryWrapper<UserPo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(poParam.getNickname())) {
            lambdaQueryWrapper.like(UserPo::getNickname, poParam.getNickname());
        }
        if (Objects.nonNull(poParam.getUsername())) {
            lambdaQueryWrapper.like(UserPo::getUsername, poParam.getUsername());
        }
        return DaoHelper.queryForPage(userMapper, pageRequest, lambdaQueryWrapper);
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'username_'+#username", unless = "#result == null")
    @Override
    public UserPo queryByUsername(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<UserPo>().eq(UserPo::getUsername, username));
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'id_'+#id", unless = "#result == null")
    @Override
    public UserPo queryById(Long id) {
        return userMapper.selectById(id);
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "'id_'+#poParam.id")
    @Override
    public Boolean updateById(UserPoParam poParam) {
        evictUsernameKeyById(poParam.getId());
        return userMapper.updateById(poParam) > 0;
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "'id_'+#id")
    @Override
    public Boolean deleteById(Long id) {
        evictUsernameKeyById(id);
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean save(UserPoParam poParam) {
        return userMapper.insert(poParam) > 0;
    }

    /**
     * 删除缓存中 "'username_'+#username" 数据
     *
     * @param id
     */
    private void evictUsernameKeyById(Long id) {
        UserPo po = userMapper.selectById(id);
        if (Objects.nonNull(po)) {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            cache.evict(String.format("username_%s", po.getUsername()));
        }
    }

}
