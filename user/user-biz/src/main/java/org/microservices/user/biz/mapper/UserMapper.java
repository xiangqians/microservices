package org.microservices.user.biz.mapper;

import org.microservices.user.biz.po.UserPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper 接口
 *
 * @author xiangqian
 * @date 23:30 2022/06/12
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPo> {

}
