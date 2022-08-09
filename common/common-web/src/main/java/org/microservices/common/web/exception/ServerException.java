package org.microservices.common.web.exception;

import lombok.Getter;
import org.microservices.common.core.resp.StatusCode;
import org.microservices.common.core.resp.Version;

/**
 * 服务异常
 *
 * @author xiangqian
 * @date 15:40:40 2022/03/19
 */
@Getter
public class ServerException extends RuntimeException {

    private Version version;
    private StatusCode statusCode;

    public ServerException(Version version, StatusCode statusCode, String message) {
        super(message);
        this.version = version;
        this.statusCode = statusCode;
    }

}
