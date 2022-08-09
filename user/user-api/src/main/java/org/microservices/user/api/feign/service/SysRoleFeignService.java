//package org.microservices.user.api.feign.service;
//
//import org.microservices.common.core.constant.ServiceNameConstants;
//import org.microservices.common.feign.annotation.FeignService;
//import org.microservices.user.common.dto.SysRoleDTO;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
///**
// * @author xiangqian
// * @date 15:00 2022/04/03
// */
//@FeignService(contextId = "SysRoleFeignService", value = ServiceNameConstants.USER)
//@RequestMapping(value = "/sys/role")
//public interface SysRoleFeignService {
//
//    @GetMapping(value = "/queryListByUserId")
//    List<SysRoleDTO> queryListByUserId(@RequestParam("userId") Integer userId);
//
//}
