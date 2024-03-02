package org.xiangqian.microservices.common.webflux;

import org.apache.commons.collections4.MapUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.condition.RequestMethodsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import org.xiangqian.microservices.common.util.AppUtil;
import org.xiangqian.microservices.common.web.RequestMapping;
import org.xiangqian.microservices.common.web.RequestMappingService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 20:43 2024/02/23
 */
public class RequestMappingServiceImpl implements RequestMappingService {

    private List<RequestMapping> requestMappings;

    private void init() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = AppUtil.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> requestMappingInfoMap = requestMappingHandlerMapping.getHandlerMethods();
        if (MapUtils.isEmpty(requestMappingInfoMap)) {
            this.requestMappings = Collections.emptyList();
            return;
        }

        List<RequestMapping> requestMappings = new ArrayList<>(requestMappingInfoMap.size());
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : requestMappingInfoMap.entrySet()) {
            RequestMapping requestMapping = new RequestMapping();

            RequestMappingInfo requestMappingInfo = entry.getKey();

            // 请求方法集
            Set<RequestMethod> requestMethods = Optional.ofNullable(requestMappingInfo.getMethodsCondition()).map(RequestMethodsRequestCondition::getMethods).orElse(null);
            requestMapping.setRequestMethods(requestMethods);

            // 请求路径
            Set<PathPattern> pathPatterns = Optional.ofNullable(requestMappingInfo.getPatternsCondition()).map(PatternsRequestCondition::getPatterns).orElse(null);
            requestMapping.setPatterns(pathPatterns.stream().map(PathPattern::getPatternString).collect(Collectors.toSet()));

            // 处理方法
            HandlerMethod handlerMethod = entry.getValue();
            requestMapping.setMethod(handlerMethod.getMethod());

            requestMappings.add(requestMapping);
        }
        this.requestMappings = Collections.unmodifiableList(requestMappings);
    }

    @Override
    public List<RequestMapping> list() {
        if (requestMappings == null) {
            synchronized (this) {
                if (requestMappings == null) {
                    init();
                }
            }
        }
        return requestMappings;
    }

}
