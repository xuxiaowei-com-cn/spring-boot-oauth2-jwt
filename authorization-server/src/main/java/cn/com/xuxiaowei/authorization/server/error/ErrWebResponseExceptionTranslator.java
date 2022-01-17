package cn.com.xuxiaowei.authorization.server.error;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * 异常翻译器
 *
 * @author xuxiaowei
 * @see DefaultWebResponseExceptionTranslator
 * @since 0.0.1
 */
public class ErrWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        throw e;
    }

}
