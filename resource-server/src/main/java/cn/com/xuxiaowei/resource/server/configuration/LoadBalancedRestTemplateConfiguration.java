package cn.com.xuxiaowei.resource.server.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 负载均衡 {@link RestTemplate} 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class LoadBalancedRestTemplateConfiguration {

    /**
     * 支持负载均衡的 {@link RestTemplate}
     *
     * @return 返回 支持负载均衡的 {@link RestTemplate}
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
