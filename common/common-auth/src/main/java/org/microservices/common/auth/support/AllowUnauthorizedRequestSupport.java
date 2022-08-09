package org.microservices.common.auth.support;

import org.microservices.common.auth.annotation.AllowUnauthorizedRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 20:31 2022/04/02
 */
public class AllowUnauthorizedRequestSupport implements ApplicationContextAware, PermitAuthorizationRequest {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Set<ResourceMatchPattern> get() {
        Map<HttpMethod, Set<String>> permitMap = new HashMap<>();
//        Set<String> patterns = new HashSet<>();
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            AllowUnauthorizedRequest allowUnauthorizedRequest = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), AllowUnauthorizedRequest.class);
            if (allowUnauthorizedRequest != null) {
                RequestMappingInfo requestMappingInfo = entry.getKey();
                requestMappingInfo.getPathPatternsCondition().getPatterns().forEach(url -> {
                    HttpMethod method = null;
                    if (handlerMethod.hasMethodAnnotation(GetMapping.class)) {
                        method = HttpMethod.GET;
                    } else if (handlerMethod.hasMethodAnnotation(PostMapping.class)) {
                        method = HttpMethod.POST;
                    } else if (handlerMethod.hasMethodAnnotation(PutMapping.class)) {
                        method = HttpMethod.PUT;
                    } else if (handlerMethod.hasMethodAnnotation(DeleteMapping.class)) {
                        method = HttpMethod.DELETE;
                    }
                    if (method != null) {
                        Set<String> patternSet = permitMap.get(method);
                        if (Objects.isNull(patternSet)) {
                            patternSet = new HashSet<>();
                            permitMap.put(method, patternSet);
                        }
                        patternSet.add(url.getPatternString().replaceAll("\\{(.*?)\\}", "*"));
                    }
                });
            }
        }

        return permitMap.entrySet().stream()
                .map(entry -> new ResourceMatchPattern(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
    }

}
