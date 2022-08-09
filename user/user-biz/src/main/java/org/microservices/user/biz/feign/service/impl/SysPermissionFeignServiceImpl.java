//package org.microservices.user.biz.feign.service.impl;
//
//import org.microservices.common.web.annotation.FeignServiceImpl;
//import org.microservices.user.api.feign.service.SysPermissionFeignService;
//import org.microservices.user.biz.service.SysPermissionService;
//import org.microservices.user.common.dto.SysPermissionDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
///**
// * @author xiangqian
// * @date 15:07 2022/04/03
// */
//@FeignServiceImpl
//public class SysPermissionFeignServiceImpl implements SysPermissionFeignService {
//
//    @Autowired
//    private SysPermissionService sysPermissionService;
//
//    @Override
//    public List<SysPermissionDTO> queryListByRoleId(Integer roleId) {
//        return sysPermissionService.queryListByRoleId(roleId);
//    }
//
//}
