package org.microservices.user.biz.service.impl;

import org.microservices.user.biz.dao.UserDao;
import org.microservices.user.biz.po.UserPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author xiangqian
 * @date 23:05 2022/07/21
 */
public abstract class AbstractService {

    @Autowired
    protected UserDao userDao;

    protected UserPo checkUserId(Long userId) {
        Assert.notNull(userId, "用户id不能为空");
        UserPo po = null;
        Assert.isTrue(userId > 0 && Objects.nonNull(po = userDao.queryById(userId)),
                String.format("用户信息（id=%s）不存在", userId));
        return po;
    }

}
