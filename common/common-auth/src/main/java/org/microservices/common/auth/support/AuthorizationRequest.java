package org.microservices.common.auth.support;

import java.util.Set;

/**
 * 授权请求
 *
 * @author xiangqian
 * @date 19:25 2022/04/02
 */
public interface AuthorizationRequest {

    Set<ResourceMatchPattern> get();

}
