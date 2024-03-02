package org.xiangqian.microservices.user.biz.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xiangqian.microservices.common.model.Page;
import org.xiangqian.microservices.common.model.PageRequest;
import org.xiangqian.microservices.common.util.Assert;
import org.xiangqian.microservices.user.biz.mapper.UserMapper;
import org.xiangqian.microservices.user.biz.service.UserService;
import org.xiangqian.microservices.user.model.entity.UserEntity;
import org.xiangqian.microservices.user.model.vo.UserAddVo;
import org.xiangqian.microservices.user.model.vo.UserPageVo;
import org.xiangqian.microservices.user.model.vo.UserUpdVo;
import org.xiangqian.microservices.user.model.vo.UserVo;

import java.util.Objects;

/**
 * 用户信息表 服务实现类
 *
 * @author xiangqian
 * @date 21:31 2023/09/04
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final String CACHE_NAME = "SERVICE_USER";

    @Autowired
    private UserMapper mapper;

    @Override
    public Page<UserVo> page(PageRequest pageRequest, UserPageVo vo) {
        return mapper.page(pageRequest, vo.copyProperties(UserEntity.class))
                .conv(entity -> entity.copyProperties(UserVo.class));
    }

    @Cacheable(cacheNames = CACHE_NAME, // 缓存名称
            key = "'id_'+#id", // 缓存key
            unless = "#result == null" // 使用条件表达式（unless）来确定是否缓存方法返回的结果，当方法返回的结果为 null 时不进行缓存。
    ) // 缓存方法返回的结果
    @Override
    public UserVo getById(Long id) {
        Assert.notNull(id, "用户id不能为空");
        Assert.isTrue(id.longValue() > 0, "用户id必须大于0");
        UserEntity entity = mapper.selectById(id);
        if (Objects.isNull(entity)) {
            return null;
        }
        return entity.copyProperties(UserVo.class);
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'name_'+#name", unless = "#result == null")
    @Override
    public UserVo getByName(String name) {
        name = StringUtils.trim(name);
        Assert.isTrue(StringUtils.isNotEmpty(name), "用户名不能为空");
        UserEntity entity = mapper.getByName(name);
        if (Objects.isNull(entity)) {
            return null;
        }
        return entity.copyProperties(UserVo.class);
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "'id_'+#vo.id") // 清除指定的缓存
    @Override
    public Boolean updById(UserUpdVo vo) {
        UserEntity entity = vo.copyProperties(UserEntity.class);
        return mapper.updateById(entity) > 0;
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "'id_'+#id")
    @Override
    public Boolean delById(Long id) {
        return mapper.deleteById(id) > 0;
    }

    @Override
    public Boolean add(UserAddVo vo) {
        UserEntity entity = vo.copyProperties(UserEntity.class);
        return mapper.insert(entity) > 0;
    }

}
