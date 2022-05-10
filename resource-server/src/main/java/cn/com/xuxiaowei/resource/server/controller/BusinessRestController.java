package cn.com.xuxiaowei.resource.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
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

    /**
     * 测试 Body
     *
     * @param request  请求
     * @param response 响应
     * @return 返回 数据
     */
    @RequestMapping("/body")
    public Map<String, Object> body(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>(8);

        map.put("body", "你好，世界");

        return map;
    }

    /**
     * 测试 Body 解密
     *
     * @param request  请求
     * @param response 响应
     * @param body     请求体
     * @return 返回 数据
     */
    @RequestMapping("/body/decrypt")
    public Map<String, Object> decrypt(HttpServletRequest request, HttpServletResponse response,
                                       @RequestBody String body) throws IOException {
        Map<String, Object> map = new HashMap<>(8);

        log.info("Controller 接收到的数据：{}", body);

        map.put("body", "你好，世界");

        return map;
    }

}