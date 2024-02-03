package org.xiangqian.microservices.common.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.xiangqian.microservices.common.model.Response;
import org.xiangqian.microservices.common.web.WebCode;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * @author xiangqian
 * @date 20:32 2023/10/13
 */
@Slf4j
public abstract class AbsController {

    protected <T> Mono<ResponseEntity<Response<T>>> mono(Supplier<? extends T> supplier) {
        return Mono.fromSupplier(() -> {
            String code = WebCode.OK;
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Response.<T>builder()
                            .code(code)
                            .msg(code)
                            .data(supplier.get())
                            .build());
        });
    }

}
