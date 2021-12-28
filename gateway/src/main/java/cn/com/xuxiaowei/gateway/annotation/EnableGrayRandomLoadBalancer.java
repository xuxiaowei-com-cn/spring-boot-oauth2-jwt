package cn.com.xuxiaowei.gateway.annotation;

import cn.com.xuxiaowei.gateway.configuration.GrayRandomLoadBalancerConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启灰度随机负载均衡注解
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@LoadBalancerClients(defaultConfiguration = GrayRandomLoadBalancerConfiguration.class)
@Import(GrayRandomLoadBalancerConfiguration.class)
public @interface EnableGrayRandomLoadBalancer {


}
