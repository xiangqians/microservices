package org.microservices.user.biz.dao;

import org.microservices.common.core.pagination.Page;
import org.microservices.common.core.pagination.PageRequest;
import org.microservices.user.biz.po.UserPo;
import org.microservices.user.biz.po.param.UserPoParam;

/**
 * @author xiangqian
 * @date 09:48 2022/04/03
 */
public interface UserDao {

    Page<UserPo> queryForPage(PageRequest pageRequest, UserPoParam poParam);

    UserPo queryByUsername(String username);

    UserPo queryById(Long id);

    Boolean updateById(UserPoParam poParam);

    Boolean deleteById(Long id);

    Boolean save(UserPoParam poParam);

}
