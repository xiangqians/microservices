//package org.microservices.user.biz.service.impl;
//
//import org.microservices.user.biz.dao.SysPermissionDao;
//import org.microservices.user.biz.service.SysPermissionService;
//import org.microservices.user.common.dato.SysPermissionDO;
//import org.microservices.user.common.dto.SysPermissionDTO;
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
// * @date 09:53 2022/04/03
// */
//@Service
//public class SysPermissionServiceImpl implements SysPermissionService {
//
//    @Autowired
//    private SysPermissionDao sysPermissionDao;
//
//    @Transactional(readOnly = true, timeout = 10)
//    @Override
//    public List<SysPermissionDTO> queryListByRoleId(Integer roleId) {
//        return Optional.ofNullable(sysPermissionDao.queryListByRoleId(roleId))
//                .map(sysPermissionDOList -> sysPermissionDOList.stream().map(SysPermissionDO::convert).collect(Collectors.toList()))
//                .orElse(null);
//    }
//
//}
