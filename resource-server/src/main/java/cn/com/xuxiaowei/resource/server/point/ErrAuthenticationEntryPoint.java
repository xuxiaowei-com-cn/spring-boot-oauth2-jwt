package cn.com.xuxiaowei.resource.server.point;

import cn.com.xuxiaowei.resource.server.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份验证入口点
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Component
public class ErrAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final TokenExtractor TOKEN_EXTRACTOR = new BearerTokenExtractor();

    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    public void setJwtAccessTokenConverter(JwtAccessTokenConverter jwtAccessTokenConverter) {
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        Map<String, Object> map = new HashMap<>(4);

        if (authException instanceof InsufficientAuthenticationException) {

            String msg;

            Authentication authentication = TOKEN_EXTRACTOR.extract(request);
            if (authentication == null) {
                msg = "无 Token";
            } else {

                String token = (String) authentication.getPrincipal();

                // Jwt Token 缓存
                JwtTokenStore tokenStore = new JwtTokenStore(jwtAccessTokenConverter);

                try {
                    OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);

                    if (accessToken == null) {
//                        throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
                        msg = "Token 无效";
                    } else if (accessToken.isExpired()) {
                        tokenStore.removeAccessToken(accessToken);
//                        throw new InvalidTokenException("Access token expired: " + accessTokenValue);
                        msg = "Token 已过期";
                    } else {
                        msg = authException.getMessage();
                    }

                } catch (Exception e) {
                    msg = "Token 无效";
                }

            }

            map.put("msg", msg);

            ResponseUtils.response(response, map);
        }

    }

}
