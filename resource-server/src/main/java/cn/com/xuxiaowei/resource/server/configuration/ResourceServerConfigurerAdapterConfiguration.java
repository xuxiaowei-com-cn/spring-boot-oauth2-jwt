package cn.com.xuxiaowei.resource.server.configuration;

import cn.com.xuxiaowei.resource.server.handler.ErrAccessDeniedHandler;
import cn.com.xuxiaowei.resource.server.point.ErrAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;

/**
 * 资源服务配置
 *
 * @author xuxiaowei
 * @see <a href="http://127.0.0.1:10301/sns/userinfo?access_token=">访问资源</a>
 * @see OAuth2AuthenticationProcessingFilter OAuth 2 认证处理过滤器
 * @since 0.0.1
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfigurerAdapterConfiguration extends ResourceServerConfigurerAdapter {

    private ErrAccessDeniedHandler errAccessDeniedHandler;

    private ErrAuthenticationEntryPoint errAuthenticationEntryPoint;

    @Autowired
    public void setErrAccessDeniedHandler(ErrAccessDeniedHandler errAccessDeniedHandler) {
        this.errAccessDeniedHandler = errAccessDeniedHandler;
    }

    @Autowired
    public void setErrAuthenticationEntryPoint(ErrAuthenticationEntryPoint errAuthenticationEntryPoint) {
        this.errAuthenticationEntryPoint = errAuthenticationEntryPoint;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources.accessDeniedHandler(errAccessDeniedHandler);
        resources.authenticationEntryPoint(errAuthenticationEntryPoint);

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置资源路径 /sns/** 需要的权限 scope
        http.antMatcher("/sns/**").authorizeRequests()
                .antMatchers("/sns/userinfo").access("#oauth2.hasAnyScope('snsapi_base','snsapi_userinfo')");

        // 配置资源路径 /sns/** 需要的权限 scope
        http.antMatcher("/test/**").authorizeRequests()
                .antMatchers("/test/userinfo").access("#oauth2.hasAnyScope('snsapi_base_A','snsapi_userinfo_B')");

        // OAuth2 授权后可访问
        http.antMatcher("/**").authorizeRequests()
                .antMatchers("/**").access("#oauth2.isOAuth()");

    }

}
