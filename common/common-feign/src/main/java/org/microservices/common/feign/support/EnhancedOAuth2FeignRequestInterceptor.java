package org.microservices.common.feign.support;

import feign.RequestTemplate;
import org.springframework.cloud.commons.security.AccessTokenContextRelay;
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * feign调用时token传递问题
 * 1、客户端带Token情况
 * 1.	如下图客户端携带token访问A服务。
 * 2.	A服务通过FeginClient 调用B服务获取相关依赖数据。
 * 3.	所以只要带token 访问A 无论后边链路有多长 ABCD 都可以获取当前用户信息
 * 4.	权限需要有这些整个链路接口的全部权限才能成功
 * 2、客户端带Token情况
 *
 * @author xiangqian
 * @date 17:11 2022/03/26
 */
public class EnhancedOAuth2FeignRequestInterceptor extends OAuth2FeignRequestInterceptor {

    private final OAuth2ClientContext oAuth2ClientContext;
    private final AccessTokenContextRelay accessTokenContextRelay;

    /**
     * Default constructor which uses the provided OAuth2ClientContext and Bearer tokens
     * within Authorization header
     *
     * @param oAuth2ClientContext            provided context
     * @param oAuth2ProtectedResourceDetails type of resource to be accessed
     * @param accessTokenContextRelay
     */
    public EnhancedOAuth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
                                                 OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails,
                                                 AccessTokenContextRelay accessTokenContextRelay) {
        super(oAuth2ClientContext, oAuth2ProtectedResourceDetails);
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.accessTokenContextRelay = accessTokenContextRelay;
    }

    /**
     * Create a template with the header of provided name and extracted extract
     * 1. 如果使用 非web 请求，header 区别
     * 2. 根据authentication 还原请求token
     *
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
//        Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
//        if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
//            return;
//        }
        accessTokenContextRelay.copyToken();
        if (oAuth2ClientContext != null && oAuth2ClientContext.getAccessToken() != null) {
            super.apply(template);
        }
    }

}
