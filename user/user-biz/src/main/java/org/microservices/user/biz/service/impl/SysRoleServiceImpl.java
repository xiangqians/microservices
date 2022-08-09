//package org.microservices.user.biz.service.impl;
//
//import org.microservices.user.biz.dao.SysRoleDao;
//import org.microservices.user.biz.service.SysRoleService;
//import org.microservices.user.common.dato.SysRoleDO;
//import org.microservices.user.common.dto.SysRoleDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
///**
// * @author xiangqian
// * @date 09:52 2022/04/03
// */
//@Service
//public class SysRoleServiceImpl implements SysRoleService {
//
//    @Autowired
//    private SysRoleDao sysRoleDao;
//
//    @Transactional(readOnly = true, timeout = 10)
//    @Override
//    public List<SysRoleDTO> queryListByUserId(Integer userId) {
//        return Optional.ofNullable(sysRoleDao.queryListByUserId(userId))
//                .map(sysRoleDOList -> sysRoleDOList.stream().map(SysRoleDO::convert).collect(Collectors.toList()))
//                .orElse(null);
//    }
//
//}
