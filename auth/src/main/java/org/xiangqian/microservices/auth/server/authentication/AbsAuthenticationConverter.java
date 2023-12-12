package org.xiangqian.microservices.auth.server.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * 抽象的认证转换器
 *
 * @author xiangqian
 * @date 19:43 2023/07/25
 */
public abstract class AbsAuthenticationConverter implements AuthenticationConverter {

    /**
     * see {@link org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2EndpointUtils#ACCESS_TOKEN_REQUEST_ERROR_URI}
     */
    protected static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    /**
     * see {@link org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2EndpointUtils#getParameters(jakarta.servlet.http.HttpServletRequest)}
     *
     * @param request
     * @return
     */
    protected static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap(parameterMap.size());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] values = entry.getValue();
            if (ArrayUtils.isNotEmpty(values)) {
                String key = entry.getKey();
                for (int i = 0, len = values.length; i < len; i++) {
                    parameters.add(key, values[i]);
                }
            }
        }
        return parameters;
    }

    /**
     * see {@link org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2EndpointUtils#throwError(java.lang.String, java.lang.String, java.lang.String)}
     *
     * @param errorCode
     * @param parameterName
     * @param errorUri
     */
    protected static void throwError(String errorCode, String parameterName, String errorUri) {
        OAuth2Error error = new OAuth2Error(errorCode, String.format("OAuth 2.0 Parameter: %s", parameterName), errorUri);
        throw new OAuth2AuthenticationException(error);
    }

}
