package org.xiangqian.microservices.common.rpc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author xiangqian
 * @date 20:54 2023/10/18
 */
@Slf4j
public class ApiFactoryBean implements FactoryBean<Object> {

    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction filter;

    // 服务名
    private String serviceName;

    // Api类型
    private Class<?> type;

    /**
     * @param serviceName 服务名
     * @param type        Api类型
     */
    public ApiFactoryBean(String serviceName, Class<?> type) {
        this.serviceName = serviceName;
        this.type = type;
    }

    @Override
    public Object getObject() throws Exception {
        // 使用 ReactorLoadBalancerExchangeFilterFunction 负载均衡通过服务名从注册中心获取地址
        WebClient client = WebClient.builder().filter(filter).baseUrl(String.format("http://%s", serviceName)).build();

        // 通过 HttpServiceProxyFactory 携带目标接口的 BaseUrl 实现 webclient 和 http interface 的关联
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client))
                .customArgumentResolver(HttpServiceArgumentResolverImpl.get())
                .build();

        // 创建客户端代理
        return factory.createClient(type);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    // 是否是单例模式
    @Override
    public boolean isSingleton() {
        return true;
    }

    public static class HttpServiceArgumentResolverImpl implements HttpServiceArgumentResolver {

        private Map<Class<?>, List<PropertyDescriptor>> typePropertyDescriptorsMap;

        public HttpServiceArgumentResolverImpl() {
            // 使用LRU算法（最近最少使用）
            this.typePropertyDescriptorsMap = new LRUMap<>(1024, 1f);
        }

        @SneakyThrows
        @Override
        public boolean resolve(Object argument, MethodParameter parameter, HttpRequestValues.Builder requestValues) {
            Class<?> parameterType = parameter.getParameterType();
            // 基本数据类型
            if (isBasicDataType(parameterType)) {
                throw new UnsupportedOperationException();
            }
            // 结构数据类型
            else {
                List<PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(parameterType);
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    Object value = propertyDescriptor.getReadMethod().invoke(argument);
                    if (Objects.nonNull(value)) {
                        String v = null;
                        if (value instanceof Collection) {
                            Collection<?> collection = (Collection<?>) value;
                            if (CollectionUtils.isNotEmpty(collection) && isBasicDataType(collection.iterator().next().getClass())) {
                                v = StringUtils.join(collection, ",");
                            }
                        } else {
                            v = value.toString();
                        }
                        requestValues.addRequestParameter(propertyDescriptor.getName(), v);
                    }
                }
            }

            return true;
        }

        private synchronized List<PropertyDescriptor> getPropertyDescriptors(Class<?> type) {
            List<PropertyDescriptor> propertyDescriptors = typePropertyDescriptorsMap.get(type);
            if (Objects.nonNull(propertyDescriptors)) {
                return propertyDescriptors;
            }

            PropertyDescriptor[] propertyDescriptorArray = BeanUtils.getPropertyDescriptors(type);
            propertyDescriptors = new ArrayList<>(propertyDescriptorArray.length);
            for (PropertyDescriptor propertyDescriptor : propertyDescriptorArray) {
                if (isCandidatePropertyDescriptor(propertyDescriptor)) {
                    propertyDescriptors.add(propertyDescriptor);
                }
            }

            typePropertyDescriptorsMap.put(type, propertyDescriptors);
            return propertyDescriptors;
        }

        private boolean isCandidatePropertyDescriptor(PropertyDescriptor propertyDescriptor) {
            String name = propertyDescriptor.getName();
            Method method = propertyDescriptor.getReadMethod();
            return Objects.nonNull(method)
                    && Modifier.isPublic(method.getModifiers())
                    && !Modifier.isStatic(method.getModifiers())
                    &&
                    (isBasicDataType(method.getReturnType())
                            // 判断returnType是否可以赋值给Collection
                            || Collection.class.isAssignableFrom(method.getReturnType()))
                    && !"class".equals(name)
                    && method.getParameterCount() == 0
                    ;
        }

        /**
         * 判断是否是基本数据类型
         *
         * @param type
         * @return
         */
        private boolean isBasicDataType(Class<?> type) {
            return type != null
                    && (type == short.class || type == Short.class
                    || type == int.class || type == Integer.class
                    || type == long.class || type == Long.class
                    || type == float.class || type == Float.class
                    || type == double.class || type == Double.class
                    || type == String.class);
        }

        private static HttpServiceArgumentResolverImpl httpServiceArgumentResolver = new HttpServiceArgumentResolverImpl();

        public static HttpServiceArgumentResolverImpl get() {
            return httpServiceArgumentResolver;
        }

    }

}
