package org.microservices.user.biz.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.microservices.common.core.pagination.Page;
import org.microservices.common.core.pagination.PageRequest;
import org.microservices.common.db.annotation.Dao;
import org.microservices.common.db.support.DaoHelper;
import org.microservices.user.biz.dao.RoleDao;
import org.microservices.user.biz.mapper.RoleMapper;
import org.microservices.user.biz.po.RolePo;
import org.microservices.user.biz.po.param.RolePoParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Objects;

/**
 * @author xiangqian
 * @date 09:49 2022/04/03
 */
@Dao
public class RoleDaoImpl implements RoleDao {

    public static final String CACHE_NAME = "CACHE_ROLE_DAO";

    @Autowired
    private RoleMapper roleMapper;

    @Cacheable(cacheNames = CACHE_NAME, key = "'userId_'+#userId", unless = "#result?.size() == 0")
    @Override
    public List<RolePo> queryForListByUserId(Long userId) {
        return roleMapper.queryForListByUserId(userId);
    }

    @Override
    public Page<RolePo> queryForPage(PageRequest pageRequest, RolePoParam poParam) {
        LambdaQueryWrapper<RolePo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(poParam.getName())) {
            lambdaQueryWrapper.like(RolePo::getName, poParam.getName());
        }
        return DaoHelper.queryForPage(roleMapper, pageRequest, lambdaQueryWrapper);
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'id_'+#id", unless = "#result == null")
    @Override
    public RolePo queryById(Long id) {
        return roleMapper.selectById(id);
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "'id_'+#poParam.id")
    @Override
    public Boolean updateById(RolePoParam poParam) {
        return roleMapper.updateById(poParam) > 0;
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "'id_'+#id")
    @Override
    public Boolean deleteById(Long id) {
        return roleMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean save(RolePoParam poParam) {
        return roleMapper.insert(poParam) > 0;
    }

}
