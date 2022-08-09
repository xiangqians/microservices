package org.microservices.user.biz.dao;

import org.microservices.common.core.pagination.Page;
import org.microservices.common.core.pagination.PageRequest;
import org.microservices.user.biz.po.RolePo;
import org.microservices.user.biz.po.param.RolePoParam;

import java.util.List;

/**
 * @author xiangqian
 * @date 09:49 2022/04/03
 */
public interface RoleDao {

    List<RolePo> queryForListByUserId(Long userId);

    Page<RolePo> queryForPage(PageRequest pageRequest, RolePoParam poParam);

    RolePo queryById(Long id);

    Boolean updateById(RolePoParam poParam);

    Boolean deleteById(Long id);

    Boolean save(RolePoParam poParam);

}
