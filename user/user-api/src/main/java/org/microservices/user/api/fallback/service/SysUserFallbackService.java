//package org.microservices.user.api.fallback.service;
//
//import org.microservices.common.core.pagination.PageDTO;
//import org.microservices.user.api.feign.service.SysUserFeignService;
//import org.microservices.user.common.dato.SysUserDO;
//import org.microservices.user.common.dto.SysUserDTO;
//import org.microservices.user.common.vo.SysUserVO;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMapping;
//
///**
// * 服务降级
// *
// * @author xiangqian
// * @date 13:14 2022/04/10
// */
//@Component
//@RequestMapping(value = "/sys/user1")
//public class SysUserFallbackService implements SysUserFeignService {
//
//    @Override
//    public PageDTO<SysUserDO, SysUserDTO, SysUserVO> page(PageDTO pageDTO) {
//        return new PageDTO<>();
//    }
//
//    @Override
//    public SysUserDTO queryByUsername(String username) {
//        return new SysUserDTO();
//    }
//
//}
