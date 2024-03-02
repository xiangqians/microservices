package org.xiangqian.microservices.user.biz.service;

import org.xiangqian.microservices.common.model.Page;
import org.xiangqian.microservices.common.model.PageRequest;
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

    UserVo getById(Long id);

    UserVo getByName(String name);

    Boolean updById(UserUpdVo vo);

    Boolean delById(Long id);

    Boolean add(UserAddVo vo);

}
