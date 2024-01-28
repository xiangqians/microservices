package org.xiangqian.microservices.user.biz.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiangqian.microservices.common.model.Page;
import org.xiangqian.microservices.common.util.Assert;
import org.xiangqian.microservices.user.biz.mapper.UserMapper;
import org.xiangqian.microservices.user.biz.service.UserService;
import org.xiangqian.microservices.user.model.entity.UserEntity;
import org.xiangqian.microservices.user.model.vo.UserAddVo;
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

    @Autowired
    private UserMapper mapper;

    @Override
    public Page<UserVo> page(Page page, UserVo vo) {
        return mapper.page(page, vo.copyProperties(UserEntity.class))
                .conv(entity -> entity.copyProperties(UserVo.class));
    }

    @Override
    public UserVo getByName(String name, boolean desensitize) {
        Assert.isTrue(StringUtils.isNotEmpty(name = StringUtils.trim(name)), "用户名不能为空");
        UserEntity entity = mapper.getByName(name);
        if (Objects.isNull(entity)) {
            return null;
        }

        if (desensitize) {
            entity.setPasswd(null);
        }

        return entity.copyProperties(UserVo.class);
    }

    @Override
    public UserVo getById(Long id, boolean desensitize) {
        Assert.notNull(id, "用户id不能为空");
        Assert.isTrue(id.longValue() > 0, "用户id必须大于0");
        UserEntity entity = mapper.selectById(id);
        if (Objects.isNull(entity)) {
            return null;
        }

        if (desensitize) {
            entity.setPasswd(null);
        }

        return entity.copyProperties(UserVo.class);
    }

    @Override
    public Boolean updById(UserUpdVo vo) {
        UserEntity entity = vo.copyProperties(UserEntity.class);
        return mapper.updateById(entity) > 0;
    }

    @Override
    public Boolean delById(Long id) {
        return mapper.deleteById(id) > 0;
    }

    @Override
    public Boolean add(UserAddVo vo) {
        UserEntity entity = vo.copyProperties(UserEntity.class);
        if (true) {
            return false;
        }
        return mapper.insert(entity) > 0;
    }

}
