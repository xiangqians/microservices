package org.microservices.user.biz.service.impl;

import org.microservices.common.core.pagination.Page;
import org.microservices.user.biz.po.UserPo;
import org.microservices.user.biz.po.param.UserPoParam;
import org.microservices.user.biz.service.UserService;
import org.microservices.user.common.vo.user.UserVo;
import org.microservices.user.common.vo.user.param.UserAddVoParam;
import org.microservices.user.common.vo.user.param.UserModifyVoParam;
import org.microservices.user.common.vo.user.param.UserPageVoParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author xiangqian
 * @date 09:50 2022/04/03
 */
@Service
public class UserServiceImpl extends AbstractService implements UserService {

    @Transactional(readOnly = true, timeout = 10)
    @Override
    public Page<UserVo> queryForPage(UserPageVoParam voParam) {
        return userDao.queryForPage(voParam, voParam.convertToPoParam(UserPoParam.class))
                .convert(po -> po.convertToVo(UserVo.class));
    }

    @Transactional(readOnly = true, timeout = 10)
    @Override
    public UserVo queryByUsername(String username) {
        UserPo po = userDao.queryByUsername(username);
        Assert.notNull(po, String.format("用户信息（username=%s）不存在", username));
        return po.convertToVo(UserVo.class);
    }

    @Transactional(readOnly = true, timeout = 10)
    @Override
    public UserVo queryById(Long id) {
        UserPo po = checkUserId(id);
        return po.convertToVo(UserVo.class);
    }

    @Transactional(timeout = 10)
    @Override
    public Boolean updateById(UserModifyVoParam voParam) {
        checkUserId(voParam.getId());
        return userDao.updateById(voParam.convertToPoParam(UserPoParam.class));
    }

    @Transactional(timeout = 10)
    @Override
    public Boolean deleteById(Long id) {
        checkUserId(id);
        return userDao.deleteById(id);
    }

    @Transactional(timeout = 10)
    @Override
    public Boolean save(UserAddVoParam voParam) {
        return userDao.save(voParam.convertToPoParam(UserPoParam.class));
    }

}
