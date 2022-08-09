package org.microservices.common.web.support;

import lombok.extern.slf4j.Slf4j;

/**
 * see
 * {@link org.springframework.security.access.expression.method.MethodSecurityExpressionOperations}
 *
 * @author xiangqian
 * @date 10:50 2022/04/02
 */
@Slf4j
public class WebMethodSecurityExpressionOperations {

    public boolean hasAnyAuthorityAndPermission(String... authorities) {
        return true;
    }

    // eg:
    // @PreAuthorize("@pre.hasAnyAuthority('ADMIN') AND @pre.hasAnyPermission('sys_user_edit')")

    public boolean hasAnyAuthority(String... authorities) {
        return true;
    }

    public boolean hasPermission(String permission) {
        return true;
    }

    public boolean hasAnyPermission(String... permissions) {
        return true;
    }

}
