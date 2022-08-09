package org.microservices.common.lock.properties;


import lombok.AllArgsConstructor;
import org.microservices.common.lock.annotation.Lock;
import org.microservices.common.lock.enumeration.LockType;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodClassKey;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author xiangqian
 * @date 22:12 2022/07/20
 */
@Component
public class LockPropertiesFactoryImpl implements LockPropertiesFactory {

    // SpEL表达式解析器
    private ExpressionParser expressionParser;
    private ParameterNameDiscoverer parameterNameDiscoverer;

    private Map<MethodClassKey, Value> cache;

    @PostConstruct
    public void init() {
        this.expressionParser = new SpelExpressionParser();
        this.parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        this.cache = new ConcurrentHashMap(1024);
    }

    @Override
    public LockProperties get(Class<?> clazz, Method method, Object[] args) {
        MethodClassKey methodClassKey = new MethodClassKey(method, clazz);
        Value value = cache.get(methodClassKey);
        if (Objects.isNull(value)) {
            synchronized (cache) {
                value = cache.get(methodClassKey);
                if (Objects.isNull(value)) {
                    Lock lock = method.getAnnotation(Lock.class);
                    String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                    Expression expression = expressionParser.parseExpression(lock.lockName());
                    value = new Value(parameterNames,
                            expression,
                            lock.lockType(),
                            lock.waitTime(),
                            lock.leaseTime(),
                            lock.timeUnit(),
                            lock.lockProvider());
                    cache.put(methodClassKey, value);
                }
            }
        }

        return value.getLockProperties(args);
    }

    @AllArgsConstructor
    private static class Value {

        private String[] parameterNames;
        private Expression expression;

        private LockType lockType;
        private long waitTime;
        private long leaseTime;
        private TimeUnit timeUnit;
        private String lockProvider;

        public String parseLockName(Object[] args) {
            // 将方法形参和实参放入上下文中
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0, length = parameterNames.length; i < length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }

            // 解析SpEL表达式获取值
            return expression.getValue(context, String.class);
        }

        public LockProperties getLockProperties(Object[] args) {
            return new LockProperties(parseLockName(args), lockType, waitTime, leaseTime, timeUnit, lockProvider);
        }

    }

}
