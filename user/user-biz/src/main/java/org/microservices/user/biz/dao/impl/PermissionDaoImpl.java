package org.microservices.user.biz.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.microservices.common.core.pagination.Page;
import org.microservices.common.core.pagination.PageRequest;
import org.microservices.common.db.annotation.Dao;
import org.microservices.common.db.support.DaoHelper;
import org.microservices.user.biz.dao.PermissionDao;
import org.microservices.user.biz.mapper.PermissionMapper;
import org.microservices.user.biz.po.PermissionPo;
import org.microservices.user.biz.po.param.PermissionPoParam;
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
public class PermissionDaoImpl implements PermissionDao {

    public static final String CACHE_NAME = "CACHE_PERMISSION_DAO";

    @Autowired
    private PermissionMapper permissionMapper;

    @Cacheable(cacheNames = CACHE_NAME, key = "'roleId_'+#roleId", unless = "#result?.size() == 0")
    @Override
    public List<PermissionPo> queryForListByRoleId(Long roleId) {
        return permissionMapper.queryForListByRoleId(roleId);
    }

    @Override
    public Page<PermissionPo> queryForPage(PageRequest pageRequest, PermissionPoParam poParam) {
        LambdaQueryWrapper<PermissionPo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(poParam.getName())) {
            lambdaQueryWrapper.like(PermissionPo::getName, poParam.getName());
        }
        return DaoHelper.queryForPage(permissionMapper, pageRequest, lambdaQueryWrapper);
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'id_'+#id", unless = "#result == null")
    @Override
    public PermissionPo queryById(Long id) {
        return permissionMapper.selectById(id);
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "'id_'+#poParam.id")
    @Override
    public Boolean updateById(PermissionPoParam poParam) {
        return permissionMapper.updateById(poParam) > 0;
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "'id_'+#id")
    @Override
    public Boolean deleteById(Long id) {
        return permissionMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean save(PermissionPoParam poParam) {
        return permissionMapper.insert(poParam) > 0;
    }

}
