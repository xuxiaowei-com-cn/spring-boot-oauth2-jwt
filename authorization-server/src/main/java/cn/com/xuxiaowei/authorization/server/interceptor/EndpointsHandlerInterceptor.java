package cn.com.xuxiaowei.authorization.server.interceptor;

import cn.com.xuxiaowei.authorization.server.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link FrameworkEndpoint} 拦截器
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
public class EndpointsHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("EndpointsHandlerInterceptor preHandle");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("EndpointsHandlerInterceptor postHandle");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("EndpointsHandlerInterceptor afterCompletion");

        Map<String, Object> map = new HashMap<>(4);

        // 由此可提醒我们：在自己编程时，针对于不同类型的错误，应推荐设置不同的异常
        if (ex instanceof InvalidClientException) {
            String clientId = request.getParameter("client_id");
            map.put("msg", String.format("client_id 无效：%s", clientId));
            ResponseUtils.response(response, map);
        } else if (ex instanceof NoSuchClientException) {
            String clientId = request.getParameter("client_id");
            map.put("msg", String.format("client_id 不存在：%s", clientId));
            ResponseUtils.response(response, map);
        } else if (ex instanceof RedirectMismatchException) {
            String redirectUri = request.getParameter("redirect_uri");
            map.put("msg", String.format("redirect_uri 未匹配到：%s", redirectUri));
            ResponseUtils.response(response, map);
        } else if (ex instanceof UnsupportedResponseTypeException) {
            String responseType = request.getParameter("response_type");
            map.put("msg", String.format("response_type 未匹配到：%s", responseType));
            ResponseUtils.response(response, map);
        } else if (ex instanceof InvalidScopeException) {
            String scope = request.getParameter("scope");
            map.put("msg", String.format("scope 未匹配到：%s", scope));
            ResponseUtils.response(response, map);
        }

    }

}
