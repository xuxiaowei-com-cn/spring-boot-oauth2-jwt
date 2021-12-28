package cn.com.xuxiaowei.resource.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@RestController
@RequestMapping("/business")
public class BusinessRestController {

    /**
     * URL
     *
     * @param request  请求
     * @param response 响应
     * @return 返回 数据
     */
    @RequestMapping("/url")
    public ResponseEntity<Map<String, Object>> name(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(8);

        int serverPort = request.getServerPort();
        String requestUri = request.getRequestURI();
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        String contextPath = request.getContextPath();

        String url = String.format("%s://%s:%s%s%s", scheme, serverName, serverPort, contextPath, requestUri);

        map.put("url", url);

        return ResponseEntity.ok(map);
    }

}
