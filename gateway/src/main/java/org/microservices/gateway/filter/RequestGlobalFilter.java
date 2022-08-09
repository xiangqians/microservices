package org.microservices.gateway.filter;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.microservices.common.core.env.CoreEnvironmentAware;
import org.microservices.gateway.service.RouteService;
import org.microservices.gateway.service.impl.RouteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiangqian
 * @date 20:57:10 2022/03/20
 */
@Slf4j
@Component
public class RequestGlobalFilter extends CoreEnvironmentAware implements GlobalFilter, ApplicationListener<RouteEvent>, Ordered {

    @Autowired
    private RouteService routeService;

    private volatile List<SafePathMatch> safePathMatches;

    public RequestGlobalFilter() {
        safePathMatches = new ArrayList<>();
    }

    /**
     * 拦截feign请求，feign接口只允许内部调用
     * 拦截monitor请求，monitor接口只允许内部调用
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(RouteEvent event) {
        List<RouteDefinition> routeDefinitions = routeService.list();
        if (CollectionUtils.isEmpty(routeDefinitions)) {
            return;
        }

        List<SafePathMatch> newSafePathMatches = new ArrayList<>();
        newSafePathMatches.add(new SafePathMatch("/*", null));
        for (RouteDefinition routeDefinition : routeDefinitions) {
            //
            List<PredicateDefinition> predicateDefinitions = null;
            if (ObjectUtils.allNotNull(routeDefinition) && CollectionUtils.isEmpty(predicateDefinitions = routeDefinition.getPredicates())) {
                continue;
            }

            for (PredicateDefinition predicateDefinition : predicateDefinitions) {
                Map<String, String> args = null;
                if (ObjectUtils.allNotNull(predicateDefinition) && !CollectionUtils.isEmpty(args = predicateDefinition.getArgs())) {
                    String genkey0 = args.get("_genkey_0");

//                    newSafePathMatches.add(new SafePathMatch(genkey0, feignBasePath));

                    // tmp test
                    if ("MONITOR-SERVICE".equals(routeDefinition.getId())) {
                        newSafePathMatches.add(new SafePathMatch(genkey0, null));
                    } else {
                        newSafePathMatches.add(new SafePathMatch(genkey0, getFeignBasePath(), getMonitorBasePath()));
                    }

                }
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append('\n');
        newSafePathMatches.forEach(safePathMatch -> builder.append('\t').append(safePathMatch).append('\n'));
        log.info("newSafePathMatches {}", builder);
        safePathMatches = newSafePathMatches;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI uri = exchange.getRequest().getURI();

        /**
         * {@link Ordered#getOrder()} = +{@link Integer#MAX_VALUE}, rawPath: /applications
         * {@link Ordered#getOrder()} = -{@link Integer#MAX_VALUE}, rawPath: /monitor/applications
         */
        String rawPath = uri.getRawPath();
        if (CollectionUtils.isEmpty(safePathMatches)
                || !safePathMatches.stream().anyMatch(safePathMatch -> safePathMatch.isMatch(rawPath))) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 执行到下一个filter
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -Integer.MAX_VALUE;
    }

    @ToString
    public static class SafePathMatch {
        private PathMatcher pathMatcher;
        private String permitPattern;
        private List<String> denyPatterns; // pattern中没有 '/' 前缀

        public SafePathMatch(String permitPattern, String... denyPatterns) {
            this.pathMatcher = new AntPathMatcher();
            this.permitPattern = permitPattern;

            // denyPatterns
            if (ObjectUtils.allNotNull(denyPatterns)) {
                this.denyPatterns = new ArrayList<>();
                for (String denyPattern : denyPatterns) {
                    if (denyPattern.startsWith("/")) {
                        denyPattern = denyPattern.substring(1);
                    }
                    denyPattern += denyPattern.endsWith("/") ? "**" : "/**";
                    this.denyPatterns.add(denyPattern);
                }
            }
        }

        public boolean isMatch(String path) {
            if (!pathMatcher.match(permitPattern, path)) {
                return false;
            }

            String extractPathWithinPattern = pathMatcher.extractPathWithinPattern(permitPattern, path);
            return CollectionUtils.isEmpty(denyPatterns) ? true : !denyPatterns.stream().anyMatch(denyPattern -> pathMatcher.match(denyPattern, extractPathWithinPattern));
        }
    }

}
