package cn.com.xuxiaowei.resource.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;

/**
 * Spring Security 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurerAdapterConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 禁用登录
        http.formLogin().disable();

        NimbusJwtDecoder.JwkSetUriJwtDecoderBuilder jwkSetUriJwtDecoderBuilder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri);
        // 负载均衡的 RestTemplate
        jwkSetUriJwtDecoderBuilder.restOperations(restTemplate);
        NimbusJwtDecoder nimbusJwtDecoder = jwkSetUriJwtDecoderBuilder.build();

        http.oauth2ResourceServer().jwt().decoder(nimbusJwtDecoder);

        // 不验证 CSRF
        http.csrf().ignoringAntMatchers("/business/body/decrypt");

    }

}
