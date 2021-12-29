package cn.com.xuxiaowei.gateway.web.reactive;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 网关 Web 异常处理程序
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Component
public class GatewayErrorWebExceptionHandler implements ErrorWebExceptionHandler, Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        MediaType contentType = request.getHeaders().getContentType();

        if (contentType == null) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        } else if (contentType.equals(MediaType.APPLICATION_JSON)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        } else {
            response.getHeaders().setContentType(contentType);
        }

        response.getHeaders().setAccept(request.getHeaders().getAccept());
        response.setStatusCode(HttpStatus.OK);

        Map<String, Object> map = new HashMap<>(4);

        if (ex instanceof ResponseStatusException) {
            ResponseStatusException rse = (ResponseStatusException) ex;

            String message = rse.getMessage();

            HttpStatus status = rse.getStatus();
            int statusValue = status.value();

            map.put("message", message);

            map.put("status", status);
            map.put("statusValue", statusValue);
            map.put("msg", "服务未找到");

        } else if (ex instanceof RuntimeException) {

            RuntimeException re = (RuntimeException) ex;

            String message = re.getMessage();

            map.put("message", message);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            byte[] bytes;
            try {
                bytes = objectMapper.writeValueAsBytes(map);
            } catch (JsonProcessingException e) {
                String msg = String.format("网关 Web 异常处理程序：%s 将响应数据处理成 byte 异常", this.getClass());
                bytes = msg.getBytes(StandardCharsets.UTF_8);
                log.error(msg, e);
            }
            return bufferFactory.wrap(bytes);
        }));
    }

}
