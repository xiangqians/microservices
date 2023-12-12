package org.xiangqian.microservices.common.webflux;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.xiangqian.microservices.common.util.JsonUtil;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;

import java.util.Set;

/**
 * WebFlux异常自定义处理：{@link org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler}
 * 如果不自定义类异常处理器，系统将自动装配 {@link org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler}
 *
 * @author xiangqian
 * @date 21:38 2023/10/16
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration(proxyBeanMethods = false)
public class WebFluxExceptionHandler extends org.xiangqian.microservices.common.web.WebExceptionHandler implements WebExceptionHandler {

    /**
     * 参考：{@link org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler#handle(org.springframework.web.server.ServerWebExchange, java.lang.Throwable)}
     *
     * @param exchange
     * @param throwable
     * @return
     */
    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()  // response是否已经提交（如果已提交，则直接 Mono.error(throwable) 抛出异常）
                || isDisconnectedClientError(throwable)) { // 是否是客户端断开连接（如果已经断开连接，则无法将消息发送给客户端，直接 Mono.error(throwable) 抛出异常）
            return Mono.error(throwable);
        }

        HttpHeaders headers = response.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);
        byte[] bytes = JsonUtil.serializeAsBytes(handle(throwable));
        DataBuffer dataBuffer = response.bufferFactory().allocateBuffer(bytes.length).write(bytes);
        return response.writeAndFlushWith(Mono.just(ByteBufMono.just(dataBuffer)));
    }

    private static final Set<String> DISCONNECTED_CLIENT_EXCEPTIONS = Set.of("AbortedException", "ClientAbortException", "EOFException", "EofException");

    /**
     * 是否是客户端断开连接
     *
     * @param throwable
     * @return
     */
    private boolean isDisconnectedClientError(Throwable throwable) {
        if (DISCONNECTED_CLIENT_EXCEPTIONS.contains(throwable.getClass().getSimpleName())) {
            return true;
        }

        String message = StringUtils.trim(NestedExceptionUtils.getMostSpecificCause(throwable).getMessage());
        if (StringUtils.isEmpty(message)) {
            return false;
        }

        message = message.toLowerCase();
        return message.contains("broken pipe") || message.contains("connection reset by peer");
    }

}
