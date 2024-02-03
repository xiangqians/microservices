package org.xiangqian.microservices.order.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.service.annotation.PutExchange;
import org.xiangqian.microservices.common.model.Page;
import org.xiangqian.microservices.common.model.PageRequest;
import org.xiangqian.microservices.common.model.Response;
import org.xiangqian.microservices.common.util.Assert;
import org.xiangqian.microservices.common.util.JsonUtil;
import org.xiangqian.microservices.order.biz.mapper.OrderMapper;
import org.xiangqian.microservices.order.biz.service.OrderService;
import org.xiangqian.microservices.order.model.OrderCode;
import org.xiangqian.microservices.order.model.entity.OrderEntity;
import org.xiangqian.microservices.order.model.vo.OrderAddVo;
import org.xiangqian.microservices.order.model.vo.OrderPageVo;
import org.xiangqian.microservices.order.model.vo.OrderUpdVo;
import org.xiangqian.microservices.order.model.vo.OrderVo;
import org.xiangqian.microservices.user.api.UserApi;
import org.xiangqian.microservices.user.model.vo.UserPageVo;
import org.xiangqian.microservices.user.model.vo.UserUpdVo;
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
    public Page<OrderVo> page(PageRequest pageRequest, OrderPageVo vo) {
        Response<Page<UserVo>> response = userApi.page(pageRequest, new UserPageVo());
        log.info("userApi.page\n{}", JsonUtil.serializeAsPrettyString(response));

        Response<UserVo> userVoResponse = userApi.getById(1L);
        log.info("userApi.getById\n{}", JsonUtil.serializeAsPrettyString(userVoResponse));
        UserVo userVo = userVoResponse.getData();
        userVo.setName(String.valueOf(System.currentTimeMillis()));
        userVo.setPasswd("123456");
        log.info("userApi.updById\n{}", userApi.updById(userVo.copyProperties(UserUpdVo.class)));
        userVoResponse = userApi.getById(1L);
        log.info("userApi.getById\n{}", JsonUtil.serializeAsPrettyString(userVoResponse));

        LambdaQueryWrapper<OrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        Page<OrderEntity> entityPage = mapper.selectPage(pageRequest, queryWrapper);
        Page<OrderVo> voPage = entityPage.conv(entity -> entity.copyProperties(OrderVo.class));
        return voPage;
    }

    @Override
    public OrderVo getById(Long id) {
        Assert.notNull(id, OrderCode.ORDER_ID_NOT_NULL);
        Assert.isTrue(id.longValue() > 0, OrderCode.ORDER_ID_MIN);
        OrderEntity entity = mapper.selectById(id);
        if (entity != null) {
            return entity.copyProperties(OrderVo.class);
        }
        return null;
    }

    @Override
    public Boolean updById(OrderUpdVo vo) {
        OrderEntity entity = vo.copyProperties(OrderEntity.class);
        return mapper.updateById(entity) > 0;
    }

    @Override
    public Boolean delById(Long id) {
        Assert.notNull(id, OrderCode.ORDER_ID_NOT_NULL);
        Assert.isTrue(id.longValue() > 0, OrderCode.ORDER_ID_MIN);
        return mapper.deleteById(id) > 0;
    }

    @Override
    public Boolean add(OrderAddVo vo) {
        OrderEntity entity = vo.copyProperties(OrderEntity.class);
        return mapper.insert(entity) > 0;
    }

}
