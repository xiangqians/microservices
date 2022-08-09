package org.microservices.common.core.annotation;

import org.microservices.common.core.support.ConditionProcessor;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.lang.annotation.*;
import java.util.List;
import java.util.Set;

/**
 * @author xiangqian
 * @date 17:46 2022/04/10
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(processor = ConditionalOnBootAnnotation.BootAnnotationOnClassProcessor.class)
public @interface ConditionalOnBootAnnotation {

//    @AliasFor(annotation = Conditional.class)
    Class<?>[] value() default {};

    class BootAnnotationOnClassProcessor implements ConditionProcessor {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata, String value) {
            return matches(context, metadata);
        }

        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(ConditionalOnBootAnnotation.class.getName(), true);
            List<Object> values = attributes.get("value");
            if (CollectionUtils.isEmpty(values)) {
                return false;
            }

            BeanDefinitionRegistry beanDefinitionRegistry = context.getRegistry();
            String[] beanDefinitionNames = beanDefinitionRegistry.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
                if (!(beanDefinition instanceof AnnotatedBeanDefinition)) {
                    continue;
                }

                AnnotatedBeanDefinition annotatedGenericBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
                AnnotationMetadata genericBeanDefinition = annotatedGenericBeanDefinition.getMetadata();
                Set<String> annotationTypes = genericBeanDefinition.getAnnotationTypes();
                if (CollectionUtils.isEmpty(annotationTypes)) {
                    continue;
                }

                for (Object value : values) {
                    if (value instanceof String) {
                        if (annotationTypes.contains(value)) {
                            return true;
                        }
                    } else if (value instanceof String[]) {
                        for (String v : (String[]) value) {
                            if (annotationTypes.contains(v)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public int getOrder() {
            return 0;
        }

    }

}
