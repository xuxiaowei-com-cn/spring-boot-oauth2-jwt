package cn.com.xuxiaowei.resource.server.handler;

import cn.com.xuxiaowei.resource.server.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 访问被拒绝处理程序
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Component
public class ErrAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        Throwable cause = accessDeniedException.getCause();
        if (cause instanceof InsufficientScopeException) {
            Map<String, Object> map = new HashMap<>(4);

            InsufficientScopeException insufficientScopeException = (InsufficientScopeException) cause;
            Map<String, String> additionalInformation = insufficientScopeException.getAdditionalInformation();
            String scope = additionalInformation.get("scope");

            map.put("msg", String.format("所需 scope 为：%s", scope));

            ResponseUtils.response(response, map);
        }

    }

}
