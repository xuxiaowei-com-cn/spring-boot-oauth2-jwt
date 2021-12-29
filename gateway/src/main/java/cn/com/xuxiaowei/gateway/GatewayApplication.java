package cn.com.xuxiaowei.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.web.reactive.function.server.ServerRequest;

/**
 * @see DefaultErrorWebExceptionHandler 默认 Web 异常处理程序
 * @see DefaultErrorWebExceptionHandler#renderErrorView(ServerRequest) 将错误信息呈现为 HTML 视图。
 * @see DefaultErrorWebExceptionHandler#renderErrorResponse(ServerRequest)  将错误信息呈现为 JSON 负载。
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
