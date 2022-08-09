package org.microservices.order.biz.support;

import org.microservices.common.auth.support.AuthorizationRequestService;
import org.microservices.common.auth.support.ResourceServerMatchPattern;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author xiangqian
 * @date 11:25 2022/04/10
 */
@Component
public class OrderResourceServerMatchPatternServiceImpl implements AuthorizationRequestService {

    // tmp test
//    @Override
//    public Set<ResourceServerMatchPattern> permitSet() {
//        return Set.of(new ResourceServerMatchPattern(HttpMethod.GET, "/**"));
//    }

}
