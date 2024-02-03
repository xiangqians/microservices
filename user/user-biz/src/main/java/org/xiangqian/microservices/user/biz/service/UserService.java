package org.xiangqian.microservices.user.biz.service;

import org.xiangqian.microservices.common.model.Page;
import org.xiangqian.microservices.common.model.PageRequest;
import org.xiangqian.microservices.user.model.entity.UserEntity;
import org.xiangqian.microservices.user.model.vo.UserAddVo;
import org.xiangqian.microservices.user.model.vo.UserPageVo;
import org.xiangqian.microservices.user.model.vo.UserUpdVo;
import org.xiangqian.microservices.user.model.vo.UserVo;

/**
 * @author xiangqian
 * @date 21:32 2023/07/20
 */
public interface UserService {

    Page<UserVo> page(PageRequest pageRequest, UserPageVo vo);

    /**
     * 根据用户名获取用户信息
     *
     * @param name        {@link UserEntity#getName()}
     * @param desensitize 是否脱敏
     * @return
     */
    UserVo getByName(String name, boolean desensitize);

    /**
     * 根据用户id获取用户信息
     *
     * @param id          {@link UserEntity#getId()}
     * @param desensitize 是否脱敏
     * @return
     */
    UserVo getById(Long id, boolean desensitize);

    Boolean updById(UserUpdVo vo);

    Boolean delById(Long id);

    Boolean add(UserAddVo vo);

}
