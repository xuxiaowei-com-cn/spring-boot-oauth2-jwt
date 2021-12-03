package cn.com.xuxiaowei.authorization.server.configuration;

import cn.com.xuxiaowei.authorization.server.properties.RsaKeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 默认 Token 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class DefaultTokenConfiguration {

    private RsaKeyProperties rsaKeyProperties;

    @Autowired
    public void setRsaKeyProperties(RsaKeyProperties rsaKeyProperties) {
        this.rsaKeyProperties = rsaKeyProperties;
    }

    /**
     * 加密 Token {@link Bean}
     * <p>
     * 在 {@link JwtAccessTokenConverter} 对应的 {@link Bean} 不存在时，才会创建此 {@link Bean}
     *
     * @return 在 {@link JwtAccessTokenConverter} 对应的 {@link Bean} 不存在时，才会返回此 {@link Bean}
     * @throws InvalidKeyException 秘钥不合法
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws InvalidKeyException {
        // 加密 Token
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        RSAPrivateKey rsaPrivateKey = rsaKeyProperties.rsaPrivateKey();
        RSAPublicKey rsaPublicKey = rsaKeyProperties.rsaPublicKey();
        jwtAccessTokenConverter.setSigner(new RsaSigner(rsaPrivateKey));
        // 可省略公钥
        jwtAccessTokenConverter.setVerifier(new RsaVerifier(rsaPublicKey));
        return jwtAccessTokenConverter;
    }

    /**
     * Jwt Token 缓存
     * 在 {@link TokenStore} 对应的 {@link Bean} 不存在时，才会创建此 {@link Bean}
     *
     * @return 在 {@link TokenStore} 对应的 {@link Bean} 不存在时，才会返回此 {@link Bean}
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

}
