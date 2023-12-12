package org.xiangqian.microservices.order.biz.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiangqian.microservices.common.model.Page;
import org.xiangqian.microservices.common.util.Assert;
import org.xiangqian.microservices.common.util.JsonUtil;
import org.xiangqian.microservices.order.biz.mapper.OrderMapper;
import org.xiangqian.microservices.order.biz.service.OrderService;
import org.xiangqian.microservices.order.model.OrderCode;
import org.xiangqian.microservices.order.model.entity.OrderEntity;
import org.xiangqian.microservices.order.model.vo.OrderAddVo;
import org.xiangqian.microservices.order.model.vo.OrderUpdVo;
import org.xiangqian.microservices.order.model.vo.OrderVo;
import org.xiangqian.microservices.user.api.UserApi;
import org.xiangqian.microservices.user.model.vo.UserVo;

/**
 * 用户信息表 服务实现类
 *
 * @author xiangqian
 * @date 21:31 2023/09/04
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper mapper;

    @Autowired
    private UserApi userApi;

    @SneakyThrows
    @Override
    public Page<OrderVo> page(Page page, OrderVo vo) {
//        log.info("userApi-----{}", userApi);
        Object r = userApi.page(new Page(), new UserVo());
//        log.info(">>>>>> {}", JsonUtil.serializeAsString(r));
//        int i = 1 / 0;
        return null;
    }

    @Override
    public OrderVo getById(Long id) {
        return null;
    }

    @Override
    public Boolean updById(OrderUpdVo vo) {
        return null;
    }

    @Override
    public Boolean delById(Long id) {
        Assert.notNull(id, OrderCode.ORDER_ID_NOT_EMPTY);
        Assert.isTrue(id.longValue() > 0, OrderCode.ORDER_ID_MUST_GT_0);
        return mapper.deleteById(id) > 0;
    }

    @Override
    public Boolean add(OrderAddVo vo) {
        OrderEntity entity = vo.copyProperties(OrderEntity.class);
        return mapper.insert(entity) > 0;
    }

}
