package cn.com.xuxiaowei.authorization.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RedisAuthorizationCodeServices;

import javax.sql.DataSource;

/**
 * 默认 授权 Code 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class DefaultAuthorizationCodeConfiguration {

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 授权 Code {@link Bean}
     * <p>
     * 在 {@link AuthorizationCodeServices} 对应的 {@link Bean} 不存在时，才会创建此 {@link Bean}
     *
     * @return 在 {@link AuthorizationCodeServices} 对应的 {@link Bean} 不存在时，才会返回此 {@link Bean}
     * @see InMemoryAuthorizationCodeServices 基于内存的 code 服务
     * @see RedisAuthorizationCodeServices 基于 Redis 的 code 服务
     * @see JdbcAuthorizationCodeServices 基于 JDBC 的 code 服务
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthorizationCodeServices jdbcAuthorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource) {
            private final RandomValueStringGenerator GENERATOR = new RandomValueStringGenerator();

            @Override
            public String createAuthorizationCode(OAuth2Authentication authentication) {
                // 自定义 code 长度
                GENERATOR.setLength(32);
                String code = GENERATOR.generate();
                store(code, authentication);
                return code;
            }
        };
    }

}
