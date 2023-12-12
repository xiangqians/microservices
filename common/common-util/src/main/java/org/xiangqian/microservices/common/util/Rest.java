package org.xiangqian.microservices.common.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * HTTP REST工具
 * <p>
 * {@link org.springframework.web.client.RestTemplate} 不传递null参数
 * 源码分析：
 * {@link org.springframework.web.client.RestTemplate.HttpEntityRequestCallback#doWithRequest(org.springframework.http.client.ClientHttpRequest)}
 * {@link org.springframework.http.converter.json.MappingJackson2HttpMessageConverter#write(java.lang.Object, java.lang.reflect.Type, org.springframework.http.MediaType, org.springframework.http.HttpOutputMessage)}
 * {@link org.springframework.http.converter.json.MappingJackson2HttpMessageConverter#writeInternal(java.lang.Object, java.lang.reflect.Type, org.springframework.http.HttpOutputMessage)}
 * 由此可见，将 {@link com.fasterxml.jackson.databind.ObjectMapper} 传入 {@link org.springframework.http.converter.json.MappingJackson2HttpMessageConverter#MappingJackson2HttpMessageConverter(com.fasterxml.jackson.databind.ObjectMapper)} 构造器即可
 * 如何传？
 * {@link org.springframework.http.converter.json.MappingJackson2HttpMessageConverter#MappingJackson2HttpMessageConverter()}
 * {@link org.springframework.http.converter.json.Jackson2ObjectMapperBuilder#build()}
 * {@link org.springframework.http.converter.json.Jackson2ObjectMapperBuilder#configure(com.fasterxml.jackson.databind.ObjectMapper)}
 * {@link org.springframework.http.converter.json.Jackson2ObjectMapperBuilder#postConfigurer(java.util.function.Consumer)}
 * {@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperBuilderConfiguration#jacksonObjectMapperBuilder(org.springframework.context.ApplicationContext, java.util.List)}
 * 注意到，使用了 ConditionalOnMissingBean 注解（当容器中没有 {@link org.springframework.http.converter.json.Jackson2ObjectMapperBuilder} bean时，注入）
 * 所以，重新注入 {@link org.springframework.http.converter.json.Jackson2ObjectMapperBuilder} 即可。
 * 但是，不成功！因为：
 * {@link org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter#AllEncompassingFormHttpMessageConverter()}
 * 那只有替换 {@link org.springframework.web.client.RestTemplate#RestTemplate()} -> {@link org.springframework.web.client.RestTemplate#messageConverters} -> {@link org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter}
 *
 * @author xiangqian
 * @date 20:47 2023/10/18
 */
public class Rest {

    private final RestTemplate restTemplate;
    protected final HttpHeaders headers;

    public Rest(RestTemplate restTemplate, HttpHeaders headers) {
        this.restTemplate = restTemplate;
        this.headers = headers;
    }

    /**
     * HTTP DELETE
     *
     * @param url      请求地址
     * @param reqBody  请求报文
     * @param respType 响应类型
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> delete(String url, Object reqBody, Class<T> respType) {
        HttpEntity<Object> entity = new HttpEntity<>(reqBody, headers);
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, respType);
    }

    /**
     * HTTP PUT
     *
     * @param url      请求地址
     * @param reqBody  请求报文
     * @param respType 响应类型
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> put(String url, Object reqBody, Class<T> respType) {
        HttpEntity<Object> entity = new HttpEntity<>(reqBody, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, respType);
    }

    /**
     * HTTP POST
     *
     * @param url      请求地址
     * @param reqBody  请求报文
     * @param respType 响应类型
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> post(String url, Object reqBody, Class<T> respType) {
        HttpEntity<Object> entity = new HttpEntity<>(reqBody, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, respType);
    }

    /**
     * HTTP GET
     *
     * @param url         请求地址
     * @param reqParamMap 请求参数
     * @param respType    响应类型
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> get(String url, Map<String, ?> reqParamMap, Class<T> respType) {
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        if (CollectionUtils.isEmpty(reqParamMap)) {
            return restTemplate.exchange(url, HttpMethod.GET, entity, respType);
        }

        url = String.format("%s?%s", url, reqParamMap.keySet().stream().map(key -> String.format("%s={%s}", key, key)).collect(Collectors.joining("&")));
        return restTemplate.exchange(url, HttpMethod.GET, entity, respType, reqParamMap);
    }

}
