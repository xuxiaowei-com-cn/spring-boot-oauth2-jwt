package cn.com.xuxiaowei.resource.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 主页
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@RestController
public class IndexRestController {

    /**
     * 主页
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 权限
     * @return 返回 权限
     */
    @RequestMapping()
    public ResponseEntity<Map<String, Object>> index(HttpServletRequest request, HttpServletResponse response,
                                                     Authentication authentication) {
        Map<String, Object> map = new HashMap<>(8);

        map.put("authentication", authentication);

        HttpSession session = request.getSession();

        return ResponseEntity.ok(map);
    }

}
