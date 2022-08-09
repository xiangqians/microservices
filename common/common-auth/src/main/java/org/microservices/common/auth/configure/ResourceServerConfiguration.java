package org.microservices.common.auth.configure;

import org.apache.commons.collections4.CollectionUtils;
import org.microservices.common.auth.support.*;
import org.microservices.common.core.constant.ServiceNameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * 资源服务配置
 *
 * @author xiangqian
 * @date 12:43 2022/04/02
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private List<HttpSecurityConfigurer> httpSecurityConfigurers;

    @Autowired
    private EnhancedResourceServerSecurityConfigurer enhancedResourceServerSecurityConfiguration;

    @Autowired
    private HeaderHttpSecurityConfigurer headerConfiguration;

    @Autowired
    private AccessHttpSecurityConfigurer accessConfiguration;

    @Autowired
    private AuthorizationRequestConfigurer authorizationRequestConfiguration;

    @Value("${spring.application.name}")
    private String name;

    /**
     * see
     * {@link RemoteTokenServices#RemoteTokenServices()}
     *
     * @return
     */
    @Bean
    @LoadBalanced // 开启负载均衡能力
    public RestTemplate lbRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
                    super.handleError(response);
                }
            }
        });
        return restTemplate;
    }

    @Bean
    public TokenStore tokenStore() {
        // jwt token转换器
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * Token转换器必须与认证服务一致
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("95ebfbdfdde34fb8954d4cb63794bc8b");
        return accessTokenConverter;
    }

    /**
     * 资源服务令牌解析服务
     * <p>
     * 注意：远程校验令牌存在性能问题，但是后续使用JWT令牌则本地即可进行校验，不必远程校验了。
     */
    @Bean
    @Primary
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();

        // 设置token校验url
        remoteTokenServices.setCheckTokenEndpointUrl(String.format("http://%s:%d/oauth/check_token", ServiceNameConstants.AUTH, 3000));

        // 设置RestTemplate，使能负载均衡
        remoteTokenServices.setRestTemplate(lbRestTemplate());

        // RemoteTokenServices 中我们配置了 access_token 的校验地址、client_id、client_secret 这三个信息，
        // 当用户来资源服务器请求资源时，会携带上一个 access_token，通过这里的配置，就能够校验出 token 是否正确等。

        // 在认证服务器配置的clientId
        remoteTokenServices.setClientId("client_2");

        // 在认证服务器配置的ClientSecret
        remoteTokenServices.setClientSecret("123456");


        // 配置一个转换器，将token信息转换为用户对象
        // TODO：获取用户信息本应该是认证服务器的事吧！总感觉在这里做不合适
//        remoteTokenServices.setAccessTokenConverter(getAccessTokenConverter());

        return remoteTokenServices;
    }

    /**
     * token服务配置
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                // 指定当前资源的id，非常重要！
                // 以模块名作为资源id
                .resourceId(name)
                //
                .stateless(false)
                // 指定保存token的方式
                .tokenServices(tokenServices());

        // ???
//            .tokenStore(tokenStore);//指定保存token的方式
    }

    /**
     * 路由安全认证配置
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //
        if (CollectionUtils.isNotEmpty(httpSecurityConfigurers)) {
            for (HttpSecurityConfigurer httpSecurityConfigurer : httpSecurityConfigurers) {
                httpSecurityConfigurer.configure(http);
            }
        }

        enhancedResourceServerSecurityConfiguration.configure(http);
        headerConfiguration.configure(http);
        accessConfiguration.configure(http);
        authorizationRequestConfiguration.configure(http);

        //
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();

        // @Deprecated
//        http.authorizeRequests()
//                .antMatchers("/**").authenticated()
//                .and()
//                .csrf().disable();
    }


//    @Autowired
//    private UserDetailsService userDetailsService;
//    //转换器，将token转换为用户信息
//    private AccessTokenConverter getAccessTokenConverter() {
//        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
//        //这个类的目的是设UserDetailsService,来将token转换为用户信息，不设默认为空
//        DefaultUserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
//        userTokenConverter.setUserDetailsService(userDetailsService);
//        accessTokenConverter.setUserTokenConverter(userTokenConverter);
//        return accessTokenConverter;
//    }
    //然后在订单Controller里，就可以取到用户的 id等其他属性了：
    //用 @AuthenticationPrincipal User user 注解可以取出User对象。
    //用 @AuthenticationPrincipal(expression = "#this.id") Long id  可以取出User里面的属性

}
