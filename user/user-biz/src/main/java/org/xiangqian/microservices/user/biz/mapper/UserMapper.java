package org.xiangqian.microservices.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xiangqian.microservices.common.model.Page;
import org.xiangqian.microservices.common.model.PageRequest;
import org.xiangqian.microservices.user.model.entity.UserEntity;

/**
 * 用户信息表 Mapper 接口
 *
 * @author xiangqian
 * @date 21:31 2023/09/04
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    Page<UserEntity> page(@Param("pageRequest") PageRequest pageRequest, @Param("entity") UserEntity entity);

    UserEntity getByName(String name);

}
