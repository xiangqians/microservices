package org.microservices.common.auth.support;

import org.microservices.common.core.support.ConditionProcessor;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 资源服务条件处理器
 *
 * @author xiangqian
 * @date 17:02 2022/04/10
 */
public class ResourceServerConditionProcessor implements ConditionProcessor {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata, String value) {
        return true;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
