package cn.com.xuxiaowei.resource.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 默认 Token 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class DefaultTokenConfiguration {

    private RedisConnectionFactory connectionFactory;

    @Autowired
    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * 资源服务器令牌服务 服务接口 {@link Bean}
     * <p>
     * 在 {@link ResourceServerTokenServices} 实现类对应的 {@link Bean} 不存在时，才会创建此 {@link Bean}
     *
     * @return 在 {@link ResourceServerTokenServices} 实现类对应的 {@link Bean} 不存在时，才会返回此 {@link Bean}
     * @see ResourceServerSecurityConfigurer#tokenServices(ResourceServerTokenServices) 可缺省
     * @see JwtTokenStore Jwt Token 储存
     * @see JdbcTokenStore JDBC Token 储存
     * @see RedisTokenStore Redis Token 储存
     */
    @Bean
    @ConditionalOnMissingBean
    public ResourceServerTokenServices resourceServerTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();

        // Redis Token 缓存
        RedisTokenStore redisTokenStore = new RedisTokenStore(connectionFactory);

        // 设置 Redis Token 缓存
        tokenServices.setTokenStore(redisTokenStore);

        return tokenServices;
    }

}
