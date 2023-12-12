package org.xiangqian.microservices.user.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiangqian.microservices.common.model.Page;
import org.xiangqian.microservices.common.model.Response;
import org.xiangqian.microservices.common.webflux.AbsController;
import org.xiangqian.microservices.user.biz.service.UserService;
import org.xiangqian.microservices.user.model.vo.UserAddVo;
import org.xiangqian.microservices.user.model.vo.UserUpdVo;
import org.xiangqian.microservices.user.model.vo.UserVo;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 21:27 2023/09/01
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口", description = "用户接口")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class UserController extends AbsController {

    @Autowired
    private UserService service;

    @GetMapping("/test")
    @Operation(summary = "test", description = "test")
    public Mono<ResponseEntity<Response<LocalDateTime>>> test() {
        return mono(() -> LocalDateTime.now());
    }

    // @ParameterObject
    // The usage of @ParameterObject is wrong. This annotation extracts fields from parameter object.
    // You should simply use @Parameter swagger standard annotation instead, or mark the parameter explicitly as @RequestParam.
    //
    // @PostMapping(value = "/persons")
    // public void create(@Parameter(in = ParameterIn.QUERY) Long id, @RequestBody Object o){}
    // I have added control to prevent this error on bad usage of @ParameterObject annotation.
    //
    // Additionally, with the next release, it will be detected out of the box, without any extra swagger-annotations.
    //
    // @ParameterObject is designed for HTTP GET Methods.
    // Please have a look at the documentation:
    // https://springdoc.org/faq.html#how-can-i-map-pageable-spring-date-commons-object-to-correct-url-parameter-in-swagger-ui

    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "分页查询用户")
    public Mono<ResponseEntity<Response<Page<UserVo>>>> page(@ParameterObject Page page, @ParameterObject UserVo vo) {
        log.info("begin");
        Mono<ResponseEntity<Response<Page<UserVo>>>> mono = mono(() -> service.page(page, vo));
        log.info("end");
        return mono;
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询用户", description = "根据id查询用户")
    public Mono<ResponseEntity<Response<UserVo>>> getById(@Parameter(description = "用户id") @PathVariable("id") Long id) {
        return mono(() -> service.getById(id, true));
    }

    @PutMapping
    @Operation(summary = "修改用户", description = "根据id修改用户")
    public Mono<ResponseEntity<Response<Boolean>>> updById(@RequestBody @Validated UserUpdVo vo) {
        return mono(() -> service.updById(vo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据id删除用户")
    public Mono<ResponseEntity<Response<Boolean>>> delById(@Parameter(description = "用户id") @PathVariable("id") Long id) {
        return mono(() -> service.delById(id));
    }

    @PostMapping
    @Operation(summary = "新增用户", description = "新增用户")
    public Mono<ResponseEntity<Response<Boolean>>> add(@RequestBody @Validated UserAddVo vo) {
        return mono(() -> service.add(vo));
    }

}
