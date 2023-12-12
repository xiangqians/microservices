package org.xiangqian.microservices.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 应用工具类
 *
 * @author xiangqian
 * @date 19:45 2023/07/07
 */
@Configuration(proxyBeanMethods = false)
public class AppUtil implements ApplicationContextAware {

    // 应用程序上下文
    private static ApplicationContext applicationContext;

    // 应用程序名称
    private static String name;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        initNameV1();
    }

    private void initNameV1() {
        Environment environment = getBean(Environment.class);
        name = environment.getProperty("spring.application.name");
    }

    private void initNameV2() {
        Yaml yaml = new Yaml(Thread.currentThread().getContextClassLoader().getResourceAsStream("bootstrap.yml"), true);
        name = yaml.getString("spring.application.name");
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 获取应用程序名称
     *
     * @return
     */
    public static String getName() {
        return name;
    }

//    private static List<ReqMapping> reqMappings;
//
//    /**
//     * 获取请求映射集
//     * {@link org.springframework.web.bind.annotation.RequestMapping}
//     */
//    public static List<ReqMapping> getReqMappings() {
//        if (Objects.isNull(reqMappings)) {
//            synchronized (SpringUtil.class) {
//                if (Objects.isNull(reqMappings)) {
//                    Map<?, HandlerMethod> reqMappingMap = null;
//                    Object handlerMapping = applicationContext.getBean("requestMappingHandlerMapping");
//                    if (handlerMapping instanceof org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping) {
//                        org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping webmvcHandlerMapping = (org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping) handlerMapping;
//                        reqMappingMap = webmvcHandlerMapping.getHandlerMethods();
//                    } else if (handlerMapping instanceof org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping) {
//                        org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping webfluxHandlerMapping = (org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping) handlerMapping;
//                        reqMappingMap = webfluxHandlerMapping.getHandlerMethods();
//                    }
//
//                    if (MapUtils.isEmpty(reqMappingMap)) {
//                        reqMappings = Collections.emptyList();
//                    } else {
//                        List<ReqMapping> reqMappings0 = new ArrayList<>(reqMappingMap.size());
//                        for (Map.Entry<?, HandlerMethod> entry : reqMappingMap.entrySet()) {
//                            Object reqMappingInfo = entry.getKey();
//                            Set<PathPattern> patterns = null;
//                            Set<RequestMethod> methods = null;
//                            if (reqMappingInfo instanceof org.springframework.web.servlet.mvc.method.RequestMappingInfo) {
//                                org.springframework.web.servlet.mvc.method.RequestMappingInfo webmvcReqMappingInfo = (org.springframework.web.servlet.mvc.method.RequestMappingInfo) reqMappingInfo;
//                                patterns = Optional.ofNullable(webmvcReqMappingInfo.getPathPatternsCondition()).map(org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition::getPatterns).orElse(null);
//                                methods = Optional.ofNullable(webmvcReqMappingInfo.getMethodsCondition()).map(org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition::getMethods).orElse(null);
//                            } else if (reqMappingInfo instanceof org.springframework.web.reactive.result.method.RequestMappingInfo) {
//                                org.springframework.web.reactive.result.method.RequestMappingInfo webfluxReqMappingInfo = (org.springframework.web.reactive.result.method.RequestMappingInfo) reqMappingInfo;
//                                patterns = Optional.ofNullable(webfluxReqMappingInfo.getPatternsCondition()).map(org.springframework.web.reactive.result.condition.PatternsRequestCondition::getPatterns).orElse(null);
//                                methods = Optional.ofNullable(webfluxReqMappingInfo.getMethodsCondition()).map(org.springframework.web.reactive.result.condition.RequestMethodsRequestCondition::getMethods).orElse(null);
//                            }
//
//                            if (CollectionUtils.isEmpty(patterns)) {
//                                continue;
//                            }
//
//                            ReqMapping reqMapping = new ReqMapping();
//                            reqMapping.setPatterns(patterns.stream().map(PathPattern::getPatternString).collect(Collectors.toSet()));
//                            reqMapping.setMethods(methods);
//                            HandlerMethod method = entry.getValue();
//                            reqMapping.setMethod(method.getMethod());
//                            reqMappings0.add(reqMapping);
//                        }
//                        reqMappings = Collections.unmodifiableList(reqMappings0);
//                    }
//                }
//            }
//        }
//        return reqMappings;
//    }
//
//    @Data
//    public static class ReqMapping {
//        private Set<RequestMethod> methods;
//        private Set<String> patterns;
//        private Method method;
//
//        @Override
//        public String toString() {
//            return "ReqMapping {" +
//                    "\n\tmethods\t\t= " + methods +
//                    "\n\tpatterns\t= " + patterns +
//                    "\n\tmethod\t\t= " + method +
//                    "\n}";
//        }
//    }

}
