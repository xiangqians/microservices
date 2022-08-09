package org.microservices.user.biz.mapper;

import org.apache.ibatis.annotations.Param;
import org.microservices.common.core.pagination.Page;
import org.microservices.user.biz.po.RolePo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色表 Mapper 接口
 *
 * @author xiangqian
 * @date 23:30 2022/06/12
 */
@Mapper
public interface RoleMapper extends BaseMapper<RolePo> {

    List<RolePo> queryForListByUserId(@Param("userId") Long userId);

}
