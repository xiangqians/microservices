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
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.xiangqian.microservices.common.model.Vo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
                .customArgumentResolver(new VoArgumentResolver())
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

    public static class VoArgumentResolver implements HttpServiceArgumentResolver {

        // 使用LRU算法（最近最少使用）
        private static final Map<Class<?>, List<PropertyDescriptor>> typePropertyDescriptorsMap = new LRUMap<>(1024, 1f);

        @SneakyThrows
        @Override
        public boolean resolve(Object argument, MethodParameter parameter, HttpRequestValues.Builder requestValues) {
            Executable executable = parameter.getExecutable();
            if (executable.isAnnotationPresent(GetExchange.class)) {
                Class<?> parameterType = parameter.getParameterType();
                if (Vo.class.isAssignableFrom(parameterType)) {
                    List<PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(parameterType);
                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        Object value = propertyDescriptor.getReadMethod().invoke(argument);
                        if (value != null) {
                            String strValue = null;
                            // 集合
                            if (value instanceof Collection) {
                                Collection<?> collection = (Collection<?>) value;
                                if (CollectionUtils.isNotEmpty(collection) && isBasicDataType(collection.iterator().next().getClass())) {
                                    strValue = StringUtils.join(collection, ",");
                                }
                            }
                            // 基本数据类型
                            else {
                                strValue = value.toString();
                            }
                            requestValues.addRequestParameter(propertyDescriptor.getName(), strValue);
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        private static synchronized List<PropertyDescriptor> getPropertyDescriptors(Class<?> type) {
            List<PropertyDescriptor> propertyDescriptors = typePropertyDescriptorsMap.get(type);
            if (propertyDescriptors != null) {
                return propertyDescriptors;
            }

            PropertyDescriptor[] propertyDescriptorArray = BeanUtils.getPropertyDescriptors(type);
            propertyDescriptors = new ArrayList<>(propertyDescriptorArray.length);
            for (PropertyDescriptor propertyDescriptor : propertyDescriptorArray) {
                String name = propertyDescriptor.getName();
                Method method = propertyDescriptor.getReadMethod();
                if (method != null
                        && Modifier.isPublic(method.getModifiers())
                        && !Modifier.isStatic(method.getModifiers())
                        &&
                        (isBasicDataType(method.getReturnType())
                                // 判断returnType是否可以赋值给Collection
                                || Collection.class.isAssignableFrom(method.getReturnType()))
                        && !"class".equals(name)
                        && method.getParameterCount() == 0) {
                    propertyDescriptors.add(propertyDescriptor);
                }
            }

            typePropertyDescriptorsMap.put(type, propertyDescriptors);
            return propertyDescriptors;
        }

        /**
         * 判断是否是基本数据类型
         *
         * @param type
         * @return
         */
        private static boolean isBasicDataType(Class<?> type) {
            return type != null
                    && (type == short.class || type == Short.class
                    || type == int.class || type == Integer.class
                    || type == long.class || type == Long.class
                    || type == float.class || type == Float.class
                    || type == double.class || type == Double.class
                    || type == String.class);
        }
    }

}
