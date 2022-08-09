package org.microservices.common.auth.support;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author xiangqian
 * @date 14:40 2022/04/10
 */
@Component
public class AuthorizationRequestConfigurer implements HttpSecurityConfigurer {

    private List<AnonymousAuthorizationRequest> anonymousAuthorizationRequests;
    private List<PermitAuthorizationRequest> permitAuthorizationRequests;
    private List<DenyAuthorizationRequest> denyAuthorizationRequests;

    @Autowired(required = false)
    public void setAnonymousAuthorizationRequests(List<AnonymousAuthorizationRequest> anonymousAuthorizationRequests) {
        this.anonymousAuthorizationRequests = anonymousAuthorizationRequests;
    }

    @Autowired(required = false)
    public void setPermitAuthorizationRequests(List<PermitAuthorizationRequest> permitAuthorizationRequests) {
        this.permitAuthorizationRequests = permitAuthorizationRequests;
    }

    @Autowired(required = false)
    public void setDenyAuthorizationRequests(List<DenyAuthorizationRequest> denyAuthorizationRequests) {
        this.denyAuthorizationRequests = denyAuthorizationRequests;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // anonymous
        authorizeRequests(http, anonymousAuthorizationRequests, Type.ANONYMOUS);

        // permit
        authorizeRequests(http, permitAuthorizationRequests, Type.PERMIT);

        // deny
        authorizeRequests(http, denyAuthorizationRequests, Type.DENY);
    }

    private void authorizeRequests(HttpSecurity http, List<? extends AuthorizationRequest> authorizationRequests, Type type) throws Exception {
        if (CollectionUtils.isEmpty(authorizationRequests)) {
            return;
        }

        for (AuthorizationRequest authorizationRequest : authorizationRequests) {
            authorizeRequests(http, authorizationRequest.get(), type);
        }
    }

    private void authorizeRequests(HttpSecurity http, Set<ResourceMatchPattern> resourceMatchPatterns, Type type) throws Exception {
        if (CollectionUtils.isEmpty(resourceMatchPatterns)) {
            return;
        }

        for (ResourceMatchPattern resourceMatchPattern : resourceMatchPatterns) {
            HttpMethod method = resourceMatchPattern.getMethod();
            Set<String> patterns = resourceMatchPattern.getPatterns();
            if (CollectionUtils.isEmpty(patterns)) {
                continue;
            }

            switch (type) {
                case ANONYMOUS:
                    http.authorizeRequests().antMatchers(method, patterns.toArray(String[]::new)).anonymous();
                    break;

                case PERMIT:
                    http.authorizeRequests().antMatchers(method, patterns.toArray(String[]::new)).permitAll();
                    break;

                case DENY:
                    http.authorizeRequests().antMatchers(method, patterns.toArray(String[]::new)).denyAll();
                    break;
            }
        }
    }

    public static enum Type {
        ANONYMOUS,
        PERMIT,
        DENY,
    }

}
