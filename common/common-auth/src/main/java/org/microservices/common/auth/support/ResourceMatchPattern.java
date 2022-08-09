package org.microservices.common.auth.support;

import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.Set;

/**
 * 资源服务匹配模式
 *
 * @author xiangqian
 * @date 19:50 2022/04/02
 */
@Data
public class ResourceMatchPattern {

    private HttpMethod method;
    private Set<String> patterns;

    public ResourceMatchPattern(String... patterns) {
        this(null, patterns);
    }

    public ResourceMatchPattern(HttpMethod method, String... patterns) {
        this(method, Set.of(patterns));
    }

    public ResourceMatchPattern(HttpMethod method, Set<String> patterns) {
        this.method = method;
        this.patterns = patterns;
    }

}
