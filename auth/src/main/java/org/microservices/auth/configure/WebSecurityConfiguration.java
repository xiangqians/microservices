package org.microservices.auth.configure;

import org.microservices.auth.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * WEB安全配置类
 * <p>
 * 主要配置：
 * 1、权限请求配置
 * 2、认证管理器配置
 * 3、用户详情服务
 *
 * @author xiangqian
 * @date 21:57:34 2022/03/20
 */
@Configuration
// Spring Security默认是禁用注解的，要想开启注解，需要在继承WebSecurityConfigurerAdapter的类上加@EnableGlobalMethodSecurity注解，来判断用户对某个控制层的方法是否具有访问权限
// 1、@EnableGlobalMethodSecurity(securedEnabled=true)：开启@Secured 注解过滤权限
// 2、@EnableGlobalMethodSecurity(jsr250Enabled=true)：开启@RolesAllowed 注解过滤权限
// 3、@EnableGlobalMethodSecurity(prePostEnabled=true)：使用表达式时间方法级别的安全性4个注解可用
//   @PreAuthorize 在方法调用之前,基于表达式的计算结果来限制对方法的访问
//   @PostAuthorize 允许方法调用,但是如果表达式计算结果为false,将抛出一个安全性异常
//   @PostFilter 允许方法调用,但必须按照表达式来过滤方法的结果
//   @PreFilter 允许方法调用,但必须在进入方法之前过滤输入值
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 安全拦截机制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 权限请求配置
                .authorizeRequests()
                // 其他请求必须认证通过
                .anyRequest().authenticated()
                .and()

                // OAuth2 授权码模式访问/oauth/authorize返回403提示
                .httpBasic()
                .and()

                // security表单登录配置
//                .formLogin() // 允许表单登录
//                .successForwardUrl("/login-success") //自定义登录成功跳转页
//                .and()

                // 关闭跨站请求防护
                .csrf().disable();
    }

    /**
     * 认证管理器配置
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() {
        return authentication -> daoAuthenticationProvider().authenticate(authentication);
    }

    /**
     * 认证是由AuthenticationManager来管理的，
     * 但是真正进行认证的是AuthenticationManager中定义的AuthenticationProvider，用于调用userDetailsService进行验证
     */
    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * 用户详情服务
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
//        userDetailsService.createUser(User.withUsername("user_1").password(passwordEncoder().encode("123456")).authorities("ROLE_USER").build());
//        userDetailsService.createUser(User.withUsername("user_2").password(passwordEncoder().encode("1234567")).authorities("ROLE_USER").build());
//        userDetailsService.createUser(User
//                .withUsername("user_3")
//                .password(passwordEncoder().encode("1234567"))
//                .authorities("ROLE_USER")
//                .authorities("get", "delete")
//                .build());
//        return userDetailsService;
        return new UserDetailsServiceImpl();
    }

    /**
     * 密码加密器
     * BCryptPasswordEncoder是一个密码加密工具类，它可以实现不可逆的加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
