package org.microservices.auth.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.microservices.auth.service.impl.ClientDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Deserializer;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RedisAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * OAuth2授权服务器配置
 * <p>
 * {@link TokenEndpoint#postAccessToken(java.security.Principal, java.util.Map)}
 *
 * @author xiangqian
 * @date 21:54:38 2022/03/20
 */
@Slf4j
@Configuration
@EnableAuthorizationServer // 开启授权服务器的自动化配置
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    // 认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    /**
     * 客户端详情服务
     * <p>
     * 操作 oauth_client_details 数据表
     *
     * @return
     */
    @Bean
    public ClientDetailsService clientDetailsServiceImpl() {
        return new ClientDetailsServiceImpl();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            // 重写token增强方法
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                String userName = authentication.getUserAuthentication().getName();
//                UserPo user = authentication.getUserAuthentication().getPrincipal();
//                /** 自定义一些token属性 ***/
//                final Map<String, Object> additionalInformation = new HashMap<>();
//                additionalInformation.put("userName", userName);
//                additionalInformation.put("roles", user.getAuthorities());
//                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
//                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
//                return enhancedToken;
                return super.enhance(accessToken, authentication);
            }
        };
        // 对称密钥，资源服务器使用该密钥来验证
        converter.setSigningKey("95ebfbdfdde34fb8954d4cb63794bc8b");
        return converter;
    }

    /**
     * 客户端详情服务配置
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsServiceImpl());
    }

    /**
     * 配置访问令牌端点
     *
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // 认证管理器
                // 配置密码模式所需要认证管理器
                .authenticationManager(authenticationManager)
                // 授权码服务，配置授权码模式所需要的服务
                .authorizationCodeServices(authorizationCodeServices)

                // 配置四种模式令牌管理服务
                .tokenServices(tokenServices())

                // 允许令牌端点请求方法类型
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        // spring Security框架默认的访问端点有：
        // 1、/oauth/authorize：获取授权码的端点
        // 2、/oauth/token：获取令牌端点。
        // 3、/oauth/confirm_access：用户确认授权提交端点。
        // 4、/oauth/error：授权服务错误信息端点。
        // 5、/oauth/check_token：用于资源服务访问的令牌解析端点。
        // 6、/oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
        // 当然如果业务要求需要改变这些默认的端点的url，也是可以修改的，AuthorizationServerEndpointsConfigurer有一个方法，如下：
        // public AuthorizationServerEndpointsConfigurer pathMapping(String defaultPath, String customPath)：
        // defaultPath：需要替换的默认端点url
        // customPath：自定义的端点url

    }

    /**
     * 令牌服务配置
     *
     * @return 令牌服务对象
     */
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);

        // 支持令牌刷新
        tokenServices.setSupportRefreshToken(true);

        // 客户端详情服务
//        tokenServices.setClientDetailsService();

        // 令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Lists.newArrayList(accessTokenConverter()));
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        // access_token令牌默认有效期2小时
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 2);

        // refresh_token刷新令牌默认有效期3天
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);

        return tokenServices;
    }

    /**
     * 配置令牌端点（Endpoint）安全约束（Spring-security拦截与否）
     *
     * @param security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {

        final String permitAll = "permitAll()";
        final String isAuthenticated = "isAuthenticated()";

        security
                // 允许 /oauth/token_key 无权限访问
                .tokenKeyAccess(permitAll)

                // /oauth/check_token 需要认证权限访问
                .checkTokenAccess(isAuthenticated)

                // 允许form表单客户端认证
                // 允许客户端使用client_id和client_secret获取token
                .allowFormAuthenticationForClients();
    }

    @Configuration
    public static class BasicConfiguration {

        private static final String PROPERTY_NAME = "microservices.auth.store.type";
        private static final String IN_MEMORY = "inMemory";
        private static final String JDBC = "jdbc";
        private static final String REDIS = "redis";

        /**
         * token持久化配置
         * Token存储策略，可以存在Redis中，也可以存在内存中，也可以结合JWT等等。
         * <p>
         * {@link TokenStore} 实现类：
         * {@link JdbcTokenStore} 操作 oauth_access_token、oauth_refresh_token（客户端grant_type需要支持refresh_token） 数据表
         * {@link RedisTokenStore}
         *
         * @return
         */
        @Bean
        @ConditionalOnProperty(name = PROPERTY_NAME, havingValue = IN_MEMORY,
                matchIfMissing = true) // 如果配置name不存在则使用此配置作为默认 TokenStore
        public TokenStore inMemoryTokenStore() {
            return new InMemoryTokenStore();
        }

        @Bean
        @ConditionalOnProperty(name = PROPERTY_NAME, havingValue = JDBC)
        public TokenStore jdbcTokenStore(DataSource dataSource) {
            return new JdbcTokenStore(dataSource);
        }

        @Bean
        @ConditionalOnProperty(name = PROPERTY_NAME, havingValue = REDIS)
        public TokenStore redisTokenStore(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
            // https://www.tabnine.com/code/java/methods/org.springframework.security.oauth2.common.util.SerializationUtils/deserialize
//            redisTokenStore.setSerializationStrategy(new JdkSerializationStrategy());

            RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
            redisTokenStore.setPrefix("microservices_");
            redisTokenStore.setAuthenticationKeyGenerator(new DefaultAuthenticationKeyGenerator() {
                @Override
                public String extractKey(OAuth2Authentication authentication) {
                    return super.extractKey(authentication);
                }
            });
            return redisTokenStore;
        }

        /**
         * Caused by: com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
         * Type id handling not implemented for type org.springframework.security.oauth2.common.OAuth2AccessToken
         * (by serializer of type org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer)
         *
         * @return
         */
//        @Bean
        public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizerImpl() {
            return jacksonObjectMapperBuilder -> {
//                jacksonObjectMapperBuilder.modules(new WebJackson2Module(), new JSR310Module());

                // https://www.javatips.net/api/easylocate-master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/common/OAuth2AccessTokenJackson2Serializer.java
                jacksonObjectMapperBuilder.serializerByType(OAuth2AccessToken.class, new OAuth2AccessTokenJackson2Serializer());
                jacksonObjectMapperBuilder.deserializerByType(OAuth2AccessToken.class, new OAuth2AccessTokenJackson2Deserializer());

                jacksonObjectMapperBuilder.serializerByType(DefaultOAuth2AccessToken.class, new OAuth2AccessTokenJackson2Serializer());
                jacksonObjectMapperBuilder.deserializerByType(DefaultOAuth2AccessToken.class, new OAuth2AccessTokenJackson2Deserializer());
            };
        }

        /**
         * 授权码服务，设置授权码模式的授权码如何存取
         * <p>
         * {@link AuthorizationCodeServices} 实现类：
         * {@link InMemoryAuthorizationCodeServices}
         * {@link JdbcAuthorizationCodeServices} 操作 oauth_code 数据表
         * {@link RedisAuthorizationCodeServices}
         * <p>
         * {@link AuthorizationServerEndpointsConfigurer#authorizationCodeServices()}
         * 如不配置 {@link InMemoryAuthorizationCodeServices} 则默认使用 {@link InMemoryAuthorizationCodeServices}
         *
         * @return
         */
        @Bean
        @ConditionalOnProperty(name = PROPERTY_NAME, havingValue = IN_MEMORY,
                matchIfMissing = true) // 如果配置name不存在则使用此配置作为默认 AuthorizationCodeServices
        public AuthorizationCodeServices inMemoryAuthorizationCodeServices() {
            return new InMemoryAuthorizationCodeServices();
        }

        @Bean
        @ConditionalOnProperty(name = PROPERTY_NAME, havingValue = JDBC)
        public AuthorizationCodeServices jdbcAuthorizationCodeServices(DataSource dataSource) {
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        @Bean
        @ConditionalOnProperty(name = PROPERTY_NAME, havingValue = REDIS)
        public AuthorizationCodeServices redisAuthorizationCodeServices(RedisConnectionFactory redisConnectionFactory) {
            return new RedisAuthorizationCodeServices(redisConnectionFactory);
        }

    }

}
