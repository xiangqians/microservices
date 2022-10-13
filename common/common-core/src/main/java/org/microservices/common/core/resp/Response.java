package org.microservices.common.core.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author xiangqian
 * @date 17:07:15 2022/03/26
 */
@Getter
@Schema(description = "响应信息")
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码")
    private int statusCode;

    @Schema(description = "消息")
    private String message;

    @Schema(description = "报文体")
    private T body;

    private Response(int statusCode, String message, T body) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = body;
    }

    public static <T> Builder<T> builder() {
        return new Builder();
    }

    public static class Builder<T> {

        private StatusCode statusCode;
        private String message;
        private T body;

        private Builder() {
        }

        public Builder statusCode(StatusCode statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder body(T body) {
            this.body = body;
            return this;
        }

        public Response build() {
            return new Response(Optional.ofNullable(statusCode).map(StatusCode::getValue).orElse(-1),
                    Optional.ofNullable(message).orElse(Optional.ofNullable(statusCode).map(StatusCode::getReasonPhrase).orElse(null)),
                    body);
        }
    }

}
