//package org.microservices.user.api.feign.service;
//
//import org.microservices.common.core.constant.ServiceNameConstants;
//import org.microservices.common.core.pagination.PageDTO;
//import org.microservices.common.feign.annotation.FeignService;
//import org.microservices.user.api.fallback.service.SysUserFallbackService;
//import org.microservices.user.common.dato.SysUserDO;
//import org.microservices.user.common.dto.SysUserDTO;
//import org.microservices.user.common.vo.SysUserVO;
//import org.springframework.cloud.openfeign.SpringQueryMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
///**
// * @author xiangqian
// * @date 12:53 2022/04/03
// */
//@FeignService(contextId = "SysUserFeignService",
//        // 服务地址
//        value = ServiceNameConstants.USER,
//        // 服务降级
//        fallback = SysUserFallbackService.class)
//@RequestMapping(value = "/sys/user")
//public interface SysUserFeignService {
//
//    /**
//     * 分页查询
//     *
//     * @param pageDTO
//     * @return
//     */
//    @GetMapping(value = "/page")
//    // Spring Cloud FeignClient调用方使用get请求传递实体类作为参数，可使用@SpringQueryMap处理
//    PageDTO<SysUserDO, SysUserDTO, SysUserVO> page(@SpringQueryMap PageDTO pageDTO);
//
//    /**
//     * 根据用户名查询
//     *
//     * @param username 用户名
//     * @return
//     */
//    @GetMapping(value = "/queryByUsername")
//    SysUserDTO queryByUsername(@RequestParam("username") String username);
//
//}
