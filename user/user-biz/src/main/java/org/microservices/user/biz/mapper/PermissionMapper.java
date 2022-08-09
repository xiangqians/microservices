package org.microservices.user.biz.mapper;

import org.apache.ibatis.annotations.Param;
import org.microservices.user.biz.po.PermissionPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 权限表 Mapper 接口
 *
 * @author xiangqian
 * @date 23:30 2022/06/12
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionPo> {

    List<PermissionPo> queryForListByRoleId(@Param("roleId") Long roleId);

}
