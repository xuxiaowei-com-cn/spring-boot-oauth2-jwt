package cn.com.xuxiaowei.resource.server.configuration;

import cn.com.xuxiaowei.resource.server.properties.RsaKeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.InvalidKeyException;
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
        RSAPublicKey rsaPublicKey = rsaKeyProperties.rsaPublicKey();
        jwtAccessTokenConverter.setVerifier(new RsaVerifier(rsaPublicKey));
        return jwtAccessTokenConverter;
    }

    /**
     * 资源服务器令牌服务 服务接口 {@link Bean}
     * <p>
     * 在 {@link ResourceServerTokenServices} 实现类对应的 {@link Bean} 不存在时，才会创建此 {@link Bean}
     *
     * @return 在 {@link ResourceServerTokenServices} 实现类对应的 {@link Bean} 不存在时，才会返回此 {@link Bean}
     * @see ResourceServerSecurityConfigurer#tokenServices(ResourceServerTokenServices) 可缺省
     */
    @Bean
    @ConditionalOnMissingBean
    public ResourceServerTokenServices resourceServerTokenServices(JwtAccessTokenConverter jwtAccessTokenConverter) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();

        // Jwt Token 缓存
        JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtAccessTokenConverter);

        // 设置 Jwt Token 缓存
        tokenServices.setTokenStore(jwtTokenStore);

        return tokenServices;
    }

}
