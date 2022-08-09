package org.microservices.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
//import org.microservices.user.api.feign.service.SysUserFeignService;
//import org.microservices.user.common.dto.SysUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Collections;

/**
 * 用户详情服务
 *
 * @author xiangqian
 * @date 18:17 2022/04/03
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

//    @Autowired
//    private SysUserFeignService sysUserApiService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUser: {}", username);

        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        if ("admin".equals(username)) {
            return getUserDetails(username);
        }

        // query
//        SysUserDTO sysUserDTO = sysUserApiService.queryByUsername(username);
//        if (Objects.isNull(sysUserDTO)) {
        throw new UsernameNotFoundException("用户不存在");
//        }
//        log.info("sysUserDTO: {}", sysUserDTO);
//
//        //
//        return new SysUserDetails(sysUserDTO);
    }

    private UserDetails getUserDetails(String username) {
        if ("admin".equals(username)) {
            return new User("admin", new BCryptPasswordEncoder().encode("123456"), true, true, true, true, Collections.emptySet());
        }
        return null;
    }


    /**
     * 用户详情
     */
//    public static class SysUserDetails implements UserDetails {
//
//        private SysUserDTO sysUserDTO;
//
//        public SysUserDetails(SysUserDTO sysUserDTO) {
//            this.sysUserDTO = sysUserDTO;
//        }
//
//        @Override
//        public Collection<? extends GrantedAuthority> getAuthorities() {
//            return Collections.emptySet();
//        }
//
//        // 密码
//        @Override
//        public String getPassword() {
//            return sysUserDTO.getPassword();
//        }
//
//        // 用户名
//        @Override
//        public String getUsername() {
//            return sysUserDTO.getUsername();
//        }
//
//        // 帐户是否未过期
//        @Override
//        public boolean isAccountNonExpired() {
//            return true;
//        }
//
//        // 帐户是否未锁定
//        @Override
//        public boolean isAccountNonLocked() {
//            return "0".equals(sysUserDTO.getLockFlag());
//        }
//
//        // 凭证是否未过期
//        @Override
//        public boolean isCredentialsNonExpired() {
//            return true;
//        }
//
//        // 是否启用
//        @Override
//        public boolean isEnabled() {
//            return "0".equals(sysUserDTO.getDelFlag());
//        }
//
//    }

}
