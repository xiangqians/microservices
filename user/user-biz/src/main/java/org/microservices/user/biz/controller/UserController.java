package org.microservices.user.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.microservices.common.core.pagination.Page;
import org.microservices.common.core.resp.Response;
import org.microservices.common.core.resp.StatusCodeImpl;
import org.microservices.user.biz.service.UserService;
import org.microservices.user.common.vo.user.UserVo;
import org.microservices.user.common.vo.user.param.UserAddVoParam;
import org.microservices.user.common.vo.user.param.UserPageVoParam;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 系统用户前端控制器
 *
 * @author xiangqian
 * @date 09:57 2022/04/03
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "用户信息管理")
public class UserController {

    @Autowired
    private UserService userService;

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
    @Operation(summary = "用户信息分页查询")
    @PreAuthorize("@pre.hasPermission('/user/page')")
    public Response<Page<UserVo>> page(@ParameterObject @Valid UserPageVoParam voParam) throws Exception {
        return Response.<Page<UserVo>>builder()
                .statusCode(StatusCodeImpl.OK)
                .body(userService.queryForPage(voParam))
                .build();
    }
//
//    @ResponseBody
//    @ApiOperation("根据id查询用户信息")
//    @GetMapping("/queryById/{id}")
//    @PreAuthorize("@pre.hasPermission('/user/queryById/{id}')")
//    @ApiImplicitParam(name = "id", value = "用户id", required = true)
//    public Response<UserVo> queryById(@PathVariable("id") Long id) throws Exception {
//        return Response.<UserVo>builder()
//                .status(Status.OK)
//                .data(userService.queryById(id))
//                .build();
//    }
//
//    @ResponseBody
//    @ApiOperation("解锁账号")
//    @PutMapping("/unlock/{id}")
//    @PreAuthorize("@pre.hasPermission('/user/unlock/{id}')")
//    @ApiImplicitParam(name = "id", value = "用户id", required = true)
//    public Response<Boolean> enable(@PathVariable("id") Long id) throws Exception {
//        return Response.<Boolean>builder()
//                .status(Status.OK)
//                .data(userService.unlock(id))
//                .build();
//    }
//
//    @ResponseBody
//    @ApiOperation("锁定账号")
//    @PutMapping("/lock/{id}")
//    @PreAuthorize("@pre.hasPermission('/user/lock/{id}')")
//    public Response<Boolean> disabled(@PathVariable("id") Long id) throws Exception {
//        return Response.<Boolean>builder()
//                .status(Status.OK)
//                .data(userService.lock(id))
//                .build();
//    }
//
//    @ResponseBody
//    @ApiOperation("修改用户信息")
//    @PutMapping("/updateById")
//    @PreAuthorize("@pre.hasPermission('/user/updateById')")
//    public Response<Boolean> updateById(@Valid UserModifyVoParameter voParameter) throws Exception {
//        return Response.<Boolean>builder()
//                .status(Status.OK)
//                .data(userService.updateById(voParameter))
//                .build();
//    }
//
//    @ResponseBody
//    @ApiOperation("删除用户信息")
//    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("@pre.hasPermission('/user/delete/{id}')")
//    @ApiImplicitParam(name = "id", value = "用户id", required = true)
//    public Response<Boolean> deleteById(@PathVariable("id") Long id) throws Exception {
//        return Response.<Boolean>builder()
//                .status(Status.OK)
//                .data(userService.deleteById(id))
//                .build();
//    }

    @PostMapping("/save")
    @Operation(summary = "新增用户信息")
    @PreAuthorize("@pre.hasPermission('/user/save')")
    public Response<Boolean> save(@Valid UserAddVoParam voParam) throws Exception {
        return Response.<Boolean>builder()
                .statusCode(StatusCodeImpl.OK)
                .body(userService.save(voParam))
                .build();
    }

//    @AllowUnauthorizedRequest
//    @GetMapping("/page")
//    @Operation(summary = "翻页查询用户信息")
//    public ResponseVO<PageVO<SysUserDO, SysUserDTO, SysUserVO>> page(PageVO pageVO) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("principal {}", principal);
//        return new ResponseVO<PageVO<SysUserDO, SysUserDTO, SysUserVO>>(Status.OK)
//                .setData(sysUserService.page(pageVO.convert()).convertToVO());
//    }
//
//    //    @Lockable(name = "'queryByUsername_'+#username")
////    @Cacheable(cacheNames = "CACHE_NAME", key = "'queryByUsername_'+#username", unless = "#result == null")
//    @AllowUnauthorizedRequest
//    @GetMapping("/queryByUsername")
//    @Operation(summary = "根据用户名查询用户信息")
//    @Parameters({@Parameter(name = "username", description = "用户名")})
////    @Retry(name = "flightSearch", fallbackMethod = "fallbackPage") // name参数的值必须与配置文件中的instances下的名称一致
//    public ResponseVO<SysUserVO> queryByUsername(@RequestParam("username") String username) {
//        log.debug("cacheManager: {}", cacheManager.getClass());
//        return new ResponseVO<SysUserVO>(Status.OK)
//                .setData(sysUserService.queryByUsername(username).convertToVO());
//    }


}

