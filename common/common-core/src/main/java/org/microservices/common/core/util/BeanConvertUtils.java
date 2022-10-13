package org.microservices.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 21:26 2022/10/13
 */
@Slf4j
public class BeanConvertUtils {

    public static <T> T convert(Object source, Class<T> targetType) throws Exception {
        PropertyDescriptor[] sourcePropertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());
        Map<String, PropertyDescriptor> sourcePropertyDescriptorMap = Arrays.stream(sourcePropertyDescriptors).collect(Collectors.toMap(PropertyDescriptor::getName, propertyDescriptor -> propertyDescriptor));
        PropertyDescriptor[] targetPropertyDescriptors = BeanUtils.getPropertyDescriptors(targetType);

        T target = targetType.getConstructor().newInstance();
        for (PropertyDescriptor targetPropertyDescriptor : targetPropertyDescriptors) {
            Method targetWriteMethod = targetPropertyDescriptor.getWriteMethod();
            Class<?>[] targetParameterTypes = null;
            if (Objects.isNull(targetWriteMethod)
                    || ArrayUtils.isEmpty(targetParameterTypes = targetWriteMethod.getParameterTypes())
                    || targetParameterTypes.length != 1) {
                continue;
            }

            PropertyDescriptor sourcePropertyDescriptor = sourcePropertyDescriptorMap.get(targetPropertyDescriptor.getName());
            Method sourceReadMethod = null;
            Object sourceValue = null;
            if (Objects.nonNull(sourcePropertyDescriptor)
                    && Objects.nonNull(sourceReadMethod = sourcePropertyDescriptor.getReadMethod())
                    && ArrayUtils.isEmpty(sourceReadMethod.getParameterTypes())
                    && Objects.nonNull(sourceValue = sourceReadMethod.invoke(source))) {

                Class<?> targetParameterType = targetParameterTypes[0];
                Object targetValue = null;

                // 基本数据类型
                if (targetParameterType == char.class) {
                } else if (targetParameterType == short.class) {

                } else if (targetParameterType == int.class) {

                } else if (targetParameterType == float.class) {

                } else if (targetParameterType == double.class) {

                } else if (targetParameterType == long.class) {

                }
                // 引用数据类型
                else if (targetParameterType == Character.class) {

                } else if (targetParameterType == Short.class) {

                } else if (targetParameterType == Integer.class) {
                    targetValue = NumberUtils.toInt(sourceValue.toString());

                } else if (targetParameterType == Float.class) {

                } else if (targetParameterType == Double.class) {

                } else if (targetParameterType == Long.class) {

                } else if (targetParameterType == String.class) {
                    targetValue = sourceValue.toString();
                }

                if (Objects.nonNull(targetValue)) {
                    targetWriteMethod.invoke(target, targetValue);
                }
            }
        }
        return target;
    }

    public static <T> T toNumber() {
        return null;
    }

}
