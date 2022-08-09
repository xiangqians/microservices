package org.microservices.user.biz.dao;

import org.microservices.common.core.pagination.Page;
import org.microservices.common.core.pagination.PageRequest;
import org.microservices.user.biz.po.PermissionPo;
import org.microservices.user.biz.po.param.PermissionPoParam;

import java.util.List;

/**
 * @author xiangqian
 * @date 09:49 2022/04/03
 */
public interface PermissionDao {

    List<PermissionPo> queryForListByRoleId(Long roleId);

    Page<PermissionPo> queryForPage(PageRequest pageRequest, PermissionPoParam poParam);

    PermissionPo queryById(Long id);

    Boolean updateById(PermissionPoParam poParam);

    Boolean deleteById(Long id);

    Boolean save(PermissionPoParam poParam);

}
