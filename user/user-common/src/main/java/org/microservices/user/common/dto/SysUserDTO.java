//package org.microservices.user.common.dto;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import org.microservices.common.core.pojoold.dto.DTO;
//import org.microservices.user.common.dato.SysUserDO;
//import org.microservices.user.common.vo.SysUserVO;
//
//import java.time.LocalDateTime;
//
///**
// * @author xiangqian
// * @date 10:39:15 2022/04/03
// */
//@Getter
//@Setter
//@ToString
//public class SysUserDTO implements DTO<SysUserDO, SysUserVO> {
//
//    private Integer userId;
//    private String username;
//    private String password;
//    private String salt;
//    private String additionalInfo;
//    private String lockFlag;
//    private String delFlag;
//    private LocalDateTime createTime;
//    private LocalDateTime updateTime;
//
//    @Override
//    public SysUserDO convertToDO() {
//        SysUserDO sysUserDO = new SysUserDO();
//        sysUserDO.setUserId(getUserId());
//        sysUserDO.setUsername(getUsername());
//        sysUserDO.setPassword(getPassword());
//        sysUserDO.setSalt(getSalt());
//        sysUserDO.setAdditionalInfo(getAdditionalInfo());
//        sysUserDO.setLockFlag(getLockFlag());
//        sysUserDO.setDelFlag(getDelFlag());
//        sysUserDO.setCreateTime(getCreateTime());
//        sysUserDO.setUpdateTime(getUpdateTime());
//        return sysUserDO;
//    }
//
//    @Override
//    public SysUserVO convertToVO() {
//        SysUserVO sysUserVO = new SysUserVO();
//        sysUserVO.setUserId(getUserId());
//        sysUserVO.setUsername(getUsername());
//        sysUserVO.setCreateTime(getCreateTime());
//        sysUserVO.setUpdateTime(getUpdateTime());
//        return sysUserVO;
//    }
//
//}
