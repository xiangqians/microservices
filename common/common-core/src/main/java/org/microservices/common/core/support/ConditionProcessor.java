package org.microservices.common.core.support;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 条件处理器
 * <p>
 * {@link Condition} & {@link Ordered}
 *
 * @author xiangqian
 * @date 00:19 2022/06/28
 */
public interface ConditionProcessor extends Ordered {

    boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata, String value);

}
