package org.microservices.auth.mapper;

import org.microservices.auth.po.OAuthClientDetailsPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户端详情 Mapper 接口
 *
 * @author xiangqian
 * @date 22:31 2022/06/23
 */
@Mapper
public interface OAuthClientDetailsMapper extends BaseMapper<OAuthClientDetailsPo> {

}
