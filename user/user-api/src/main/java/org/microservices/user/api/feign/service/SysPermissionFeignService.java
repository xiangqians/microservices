//package org.microservices.user.api.feign.service;
//
//import org.microservices.common.core.constant.ServiceNameConstants;
//import org.microservices.common.feign.annotation.FeignService;
//import org.microservices.user.common.dto.SysPermissionDTO;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//
///**
// * @author xiangqian
// * @date 15:05 2022/04/03
// */
//@FeignService(contextId = "SysPermissionFeignService", value = ServiceNameConstants.USER)
//@RequestMapping(value = "/sys/permission")
//public interface SysPermissionFeignService {
//
//    @GetMapping(value = "/queryListByUserId")
//    List<SysPermissionDTO> queryListByRoleId(@RequestParam("roleId") Integer roleId);
//
//}
