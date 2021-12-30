package cn.com.xuxiaowei.resource.server.filter;

import cn.com.xuxiaowei.resource.server.utils.RequestUtils;
import cn.com.xuxiaowei.resource.server.utils.http.InputStreamHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * 请求流转换为多次读取的请求流 过滤器
 * <p>
 * 使用配置文件进行条件注解为 {@link Bean}
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Order(HIGHEST_PRECEDENCE + 1)
@Slf4j
@Component
public class HttpServletRequestInputStreamFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 将请求流转换为可多次读取的请求流
        ServletRequest inputStreamHttpServletRequestWrapper = new InputStreamHttpServletRequestWrapper(httpServletRequest);

        // 测试
        @SuppressWarnings("all")
        HttpServletRequest tempHttpServletRequest = (HttpServletRequest) inputStreamHttpServletRequestWrapper;
        String inputStream = RequestUtils.getInputStream(tempHttpServletRequest);
        String copyInputStream = RequestUtils.copyInputStream(tempHttpServletRequest);
        log.debug("手动获取请求流测试-Content-Length：{}", inputStream);
        log.debug("手动获取请求流测试-IOUtils：{}", copyInputStream);

        chain.doFilter(inputStreamHttpServletRequestWrapper, response);
    }

}
