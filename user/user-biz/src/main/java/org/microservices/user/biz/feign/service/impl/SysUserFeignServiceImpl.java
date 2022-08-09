//package org.microservices.user.biz.feign.service.impl;
//
//import org.microservices.common.auth.annotation.AllowUnauthorizedRequest;
//import org.microservices.common.core.pagination.PageDTO;
//import org.microservices.common.web.annotation.FeignServiceImpl;
//import org.microservices.user.api.feign.service.SysUserFeignService;
//import org.microservices.user.biz.service.SysUserService;
//import org.microservices.user.common.dato.SysUserDO;
//import org.microservices.user.common.dto.SysUserDTO;
//import org.microservices.user.common.vo.SysUserVO;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * @author xiangqian
// * @date 13:39 2022/04/03
// */
//@FeignServiceImpl
//public class SysUserFeignServiceImpl implements SysUserFeignService {
//
//    @Autowired
//    private SysUserService sysUserService;
//
//    @AllowUnauthorizedRequest
//    @Override
//    public PageDTO<SysUserDO, SysUserDTO, SysUserVO> page(PageDTO pageDTO) {
//        return sysUserService.page(pageDTO);
//    }
//
//    @AllowUnauthorizedRequest
//    @Override
//    public SysUserDTO queryByUsername(String username) {
//        return sysUserService.queryByUsername(username);
//    }
//
//}
