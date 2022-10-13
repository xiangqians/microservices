package org.microservices.common.core.annotation;

import lombok.extern.slf4j.Slf4j;
import org.microservices.common.core.support.ConditionProcessor;
import org.microservices.common.core.util.ResourceResolver;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 参考：
 * {@link org.springframework.context.annotation.Conditional} that only matches when the specified classes are on the classpath.
 *
 * @author xiangqian
 * @date 15:12 2022/04/10
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.springframework.context.annotation.Conditional({Conditional.ConditionImpl.class})
public @interface Conditional {

    /**
     * 条件值
     *
     * @return
     */
    String value() default "";

    /**
     * 条件处理器
     *
     * @return
     */
    Class<? extends ConditionProcessor> processor();

    @Slf4j
    class ConditionImpl implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Map<String, Object> attributes = metadata.getAnnotationAttributes(Conditional.class.getName());
            String value = (String) attributes.get("value");
            Class<ConditionProcessor> conditionProcessorClass = (Class<ConditionProcessor>) attributes.get("processor");
            Set<Class<ConditionProcessor>> candidateClassSet = null;
            try {
                candidateClassSet = ResourceResolver.getClassSet(candidateClass -> typeof(conditionProcessorClass, candidateClass), "org.microservices");
            } catch (IOException e) {
                log.error("", e);
                return false;
            }

            if (CollectionUtils.isEmpty(candidateClassSet)) {
                // 如果候选处理器为空时，则考虑使用 conditionProcessorClass
                return matches(conditionProcessorClass, context, metadata, value);
            }

            // 仅有一个候选处理器时
            if (candidateClassSet.size() == 1) {
                return matches(candidateClassSet.iterator().next(), context, metadata, value);
            }

            Map<Integer, Set<ConditionProcessor>> orderMap = new HashMap<>();
            for (Class<ConditionProcessor> c : candidateClassSet) {
                ConditionProcessor processor = newProcessor(c);
                int order = processor.getOrder();
                Set<ConditionProcessor> processorSet = orderMap.get(order);
                if (Objects.isNull(processorSet)) {
                    processorSet = new HashSet<>();
                    orderMap.put(order, processorSet);
                }
                processorSet.add(processor);
            }
            List<Integer> orderList = orderMap.keySet().stream().collect(Collectors.toList());
            Collections.sort(orderList); // 升序
            Set<ConditionProcessor> processorSet = orderMap.get(orderList.get(0));
            if (processorSet.size() == 1) {
                return processorSet.iterator().next().matches(context, metadata, value);
            } else {
                throw new RuntimeException("存在多个条件处理器，" + processorSet);
            }
        }

        private boolean matches(Class<ConditionProcessor> processorClass,
                                ConditionContext context,
                                AnnotatedTypeMetadata metadata,
                                String value) {
            return Optional.ofNullable(newProcessor(processorClass))
                    .map(processor -> processor.matches(context, metadata, value))
                    .orElse(false);
        }

        private ConditionProcessor newProcessor(Class<ConditionProcessor> processorClass) {
            if (Modifier.isPublic(processorClass.getModifiers())
                    && !Modifier.isInterface(processorClass.getModifiers())
                    && !Modifier.isAbstract(processorClass.getModifiers())) {
                try {
                    return processorClass.getConstructor().newInstance();
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            return null;
        }

        private boolean typeof(Class<?> source, Class<?> target) {
            if (Objects.isNull(target) || target == Object.class) {
                return false;
            }

            if (source == target) {
                return true;
            }

            // super class
            for (Class<?> clazz = target.getSuperclass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
                if (typeof(source, clazz)) {
                    return true;
                }
            }

            // interface
            Class<?>[] interfaces = target.getInterfaces();
            for (Class<?> clazz : interfaces) {
                if (typeof(source, clazz)) {
                    return true;
                }
            }

            return false;
        }
    }

}
