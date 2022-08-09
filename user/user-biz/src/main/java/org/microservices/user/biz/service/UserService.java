package org.microservices.user.biz.service;

import org.microservices.common.core.pagination.Page;
import org.microservices.common.core.pagination.PageRequest;
import org.microservices.user.biz.po.UserPo;
import org.microservices.user.biz.po.param.UserPoParam;
import org.microservices.user.common.vo.user.UserVo;
import org.microservices.user.common.vo.user.param.UserAddVoParam;
import org.microservices.user.common.vo.user.param.UserModifyVoParam;
import org.microservices.user.common.vo.user.param.UserPageVoParam;

/**
 * 系统用户服务类
 *
 * @author xiangqian
 * @date 09:50 2022/04/03
 */
public interface UserService {

    Page<UserVo> queryForPage(UserPageVoParam voParam);

    UserVo queryByUsername(String username);

    UserVo queryById(Long id);

    Boolean updateById(UserModifyVoParam voParam);

    Boolean deleteById(Long id);

    Boolean save(UserAddVoParam voParam);

}
