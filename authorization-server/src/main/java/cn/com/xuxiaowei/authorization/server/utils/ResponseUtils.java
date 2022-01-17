package cn.com.xuxiaowei.authorization.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 响应工具类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class ResponseUtils {

    /**
     * 响应数据
     *
     * @param response 响应
     * @param map      Map 类型的数据
     * @throws IOException IO 异常
     */
    @SuppressWarnings({"rawtypes"})
    public static void response(HttpServletResponse response, Map map) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(map);
        response.getWriter().println(json);
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

}
