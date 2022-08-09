package org.microservices.monitor.support;

import de.codecentric.boot.admin.server.ui.extensions.UiExtensions;
import de.codecentric.boot.admin.server.ui.web.UiController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author xiangqian
 * @date 17:01 2022/04/06
 */
@Slf4j
public class MonitorUiController extends UiController {

    private String publicUrl;

    public MonitorUiController(String publicUrl, UiExtensions uiExtensions, Settings uiSettings) {
        super(publicUrl, uiExtensions, uiSettings);
        this.publicUrl = publicUrl;
    }

    /**
     * 使用@Autowired注解时，Spring 实际注入的并非 HttpServletRequest 对象，而是一个代理 proxy（代理实现参考 {@link org.springframework.beans.factory.support.AutowireUtils.ObjectFactoryDelegatingInvocationHandler}）
     * 当方法中需要使用 HttpServletRequest 对象时通过此代理获取，所以虽然Controller是个单实例类（spring默认情况下），但通过此方法使用HttpServletRequest对象是线程安全的。
     * 注入的对象不局限于 HttpServletRequest 对象，还可以注入其它 scope 为 request 或 session 的对象，如 HttpServletResponse、HttpSession 等，并保证线程安全。
     */
//    @Autowired
//    private HttpServletRequest request;


    /**
     * Override
     *
     * @param uriBuilder
     * @return
     */
    @ModelAttribute(value = "baseUrl", binding = false)
    @Override
    public String getBaseUrl(UriComponentsBuilder uriBuilder) {
        String publicUrl = this.publicUrl;

        // request header
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        // x-forwarded-proto=http
        // x-forwarded-host=localhost:9999
        // x-forwarded-prefix=/monitor
        String proto = request.getHeader("x-forwarded-proto");
        if (!StringUtils.isEmpty(proto)) {
            String host = request.getHeader("x-forwarded-host");
            String prefix = request.getHeader("x-forwarded-prefix");
            log.debug("request.header['x-forwarded-proto']={}", proto);
            log.debug("request.header['x-forwarded-host']={}", host);
            log.debug("request.header['x-forwarded-prefix']={}", prefix);
            publicUrl = String.format("%s://%s%s", proto, host, prefix);
        }

        log.debug("publicUrl: {}", publicUrl);
        return getBaseUrl(uriBuilder, publicUrl);
    }

    /**
     * see {@link UiController#getBaseUrl(org.springframework.web.util.UriComponentsBuilder)}
     *
     * @param uriBuilder
     * @param publicUrl
     * @return
     */
    private String getBaseUrl(UriComponentsBuilder uriBuilder, String publicUrl) {
        UriComponents publicComponents = UriComponentsBuilder.fromUriString(publicUrl).build();
        if (publicComponents.getScheme() != null) {
            uriBuilder.scheme(publicComponents.getScheme());
        }

        if (publicComponents.getHost() != null) {
            uriBuilder.host(publicComponents.getHost());
        }

        if (publicComponents.getPort() != -1) {
            uriBuilder.port(publicComponents.getPort());
        }

        if (publicComponents.getPath() != null) {
            uriBuilder.path(publicComponents.getPath());
        }

        return uriBuilder.path("/").toUriString();
    }

    /**
     * printHeaderInfo
     */
    private void printHeaderInfo() {
        log.debug("------------ printHeaderInfo: ");
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String headerName = enumeration.nextElement();
                String headerValue = request.getHeader(headerName);
                System.out.println(String.format("%s=%s", headerName, headerValue));
            }
        }
    }

    // host=localhost:9200
    // connection=keep-alive
    // pragma=no-cache
    // cache-control=no-cache
    // sec-ch-ua=" Not A;Brand";v="99", "Chromium";v="100", "Google Chrome";v="100"
    // sec-ch-ua-mobile=?0
    // user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36
    // sec-ch-ua-platform="Windows"
    // accept=*/*
    // sec-fetch-site=same-origin
    // sec-fetch-mode=no-cors
    // sec-fetch-dest=script
    // referer=http://localhost:9200/applications
    // accept-encoding=gzip, deflate, br
    // accept-language=en,zh-CN;q=0.9,zh;q=0.8
    // baseUrl http://localhost:9200/

    // pragma=no-cache
    // cache-control=no-cache
    // sec-ch-ua=" Not A;Brand";v="99", "Chromium";v="100", "Google Chrome";v="100"
    // sec-ch-ua-mobile=?0
    // sec-ch-ua-platform="Windows"
    // upgrade-insecure-requests=1
    // user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36
    // accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
    // sec-fetch-site=none
    // sec-fetch-mode=navigate
    // sec-fetch-user=?1
    // sec-fetch-dest=document
    // accept-encoding=gzip, deflate, br
    // accept-language=en,zh-CN;q=0.9,zh;q=0.8
    // forwarded=proto=http;host="localhost:9999";for="[0:0:0:0:0:0:0:1]:54664"
    // x-forwarded-for=0:0:0:0:0:0:0:1
    // x-forwarded-proto=http
    // x-forwarded-prefix=/monitor
    // x-forwarded-port=9999
    // x-forwarded-host=localhost:9999
    // host=192.168.1.11:9200
    // content-length=0
    // baseUrl http://192.168.1.11:9200/

}
