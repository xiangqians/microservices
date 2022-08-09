//package org.microservices.user.biz.feign.service.impl;
//
//import org.microservices.common.web.annotation.FeignServiceImpl;
//import org.microservices.user.api.feign.service.SysRoleFeignService;
//import org.microservices.user.biz.service.SysRoleService;
//import org.microservices.user.common.dto.SysRoleDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
///**
// * @author xiangqian
// * @date 15:04 2022/04/03
// */
//@FeignServiceImpl
//public class SysRoleFeignServiceImpl implements SysRoleFeignService {
//
//    @Autowired
//    private SysRoleService sysRoleService;
//
//    @Override
//    public List<SysRoleDTO> queryListByUserId(Integer userId) {
//        return sysRoleService.queryListByUserId(userId);
//    }
//
//}
