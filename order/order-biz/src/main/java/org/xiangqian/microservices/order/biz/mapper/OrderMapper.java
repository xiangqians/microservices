package org.xiangqian.microservices.order.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiangqian.microservices.order.model.entity.OrderEntity;

/**
 * 订单信息表 Mapper 接口
 *
 * @author xiangqian
 * @date 20:07 2023/09/11
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
}
