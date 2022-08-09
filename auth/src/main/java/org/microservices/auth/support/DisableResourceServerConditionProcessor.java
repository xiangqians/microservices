package org.microservices.auth.support;

import org.microservices.common.auth.support.ResourceServerConditionProcessor;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 禁用资源服务条件处理器
 *
 * @author xiangqian
 * @date 17:07 2022/04/10
 */
public class DisableResourceServerConditionProcessor extends ResourceServerConditionProcessor {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata, String value) {
        return false;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
