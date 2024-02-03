package org.xiangqian.microservices.order.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiangqian.microservices.common.model.PageRequest;
import org.xiangqian.microservices.common.model.Response;
import org.xiangqian.microservices.common.model.validation.group.Add;
import org.xiangqian.microservices.common.model.validation.group.Page;
import org.xiangqian.microservices.common.model.validation.group.Upd;
import org.xiangqian.microservices.common.webmvc.AbsController;
import org.xiangqian.microservices.order.biz.service.OrderService;
import org.xiangqian.microservices.order.model.vo.OrderAddVo;
import org.xiangqian.microservices.order.model.vo.OrderPageVo;
import org.xiangqian.microservices.order.model.vo.OrderUpdVo;
import org.xiangqian.microservices.order.model.vo.OrderVo;

/**
 * @author xiangqian
 * @date 20:10 2023/09/11
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Tag(name = "订单接口", description = "订单接口")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class OrderController extends AbsController {

    @Autowired
    private OrderService service;

    @GetMapping("/page")
    @Operation(summary = "分页查询订单", description = "分页查询订单")
    public Response<org.xiangqian.microservices.common.model.Page> page(@ParameterObject PageRequest pageRequest, @ParameterObject @Validated(Page.class) OrderPageVo vo) {
        return response(service.page(pageRequest, vo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询订单", description = "根据id查询订单")
    public Response<OrderVo> getById(@Parameter(description = "订单id") @PathVariable("id") Long id) {
        return response(service.getById(id));
    }

    @PutMapping
    @Operation(summary = "修改订单", description = "根据id修改订单")
    public Response<Boolean> updById(@RequestBody @Validated(Upd.class) OrderUpdVo vo) {
        return response(service.updById(vo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单", description = "根据id删除订单")
    public Response<Boolean> delById(@Parameter(description = "订单id") @PathVariable("id") Long id) {
        return response(service.delById(id));
    }

    @PostMapping
    @Operation(summary = "新增订单", description = "新增订单")
    public Response<Boolean> add(@RequestBody @Validated(Add.class) OrderAddVo vo) {
        return response(service.add(vo));
    }

}
