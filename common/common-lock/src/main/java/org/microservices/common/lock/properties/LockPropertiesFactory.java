package org.microservices.common.lock.properties;

import java.lang.reflect.Method;

/**
 * @author xiangqian
 * @date 22:10 2022/07/20
 */
public interface LockPropertiesFactory {

    LockProperties get(Class<?> clazz, Method method, Object[] args);

}
