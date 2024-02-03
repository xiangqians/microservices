package org.xiangqian.microservices.order.biz.service;

import org.xiangqian.microservices.common.model.Page;
import org.xiangqian.microservices.common.model.PageRequest;
import org.xiangqian.microservices.order.model.vo.OrderAddVo;
import org.xiangqian.microservices.order.model.vo.OrderPageVo;
import org.xiangqian.microservices.order.model.vo.OrderUpdVo;
import org.xiangqian.microservices.order.model.vo.OrderVo;

/**
 * 订单服务
 *
 * @author xiangqian
 * @date 20:08 2023/09/11
 */
public interface OrderService {

    Page<OrderVo> page(PageRequest pageRequest, OrderPageVo vo);

    OrderVo getById(Long id);

    Boolean updById(OrderUpdVo vo);

    Boolean delById(Long id);

    Boolean add(OrderAddVo vo);

}
