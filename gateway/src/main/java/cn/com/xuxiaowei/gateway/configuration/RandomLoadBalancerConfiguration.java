package cn.com.xuxiaowei.gateway.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.client.loadbalancer.reactive.Request;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * 随机负载均衡器 {@link RandomLoadBalancer}
 * <p>
 * 默认：轮询负载均衡器 {@link RoundRobinLoadBalancer}
 * <p>
 * 使用：设置 {@link LoadBalancerClients#defaultConfiguration()} 为 {@link RandomLoadBalancerConfiguration} 进行全局启用。
 * 设置 {@link LoadBalancerClients#value()}，其中 {@link LoadBalancerClient#name()}、{@link LoadBalancerClient#value()} 标识服务，
 * {@link LoadBalancerClient#configuration()} 设置为 {@link RandomLoadBalancerConfiguration} 进行单个服务启用。
 *
 * @author xuxiaowei
 * @see LoadBalancerClientConfiguration 默认轮询负载均衡器
 * @see RandomLoadBalancerConfiguration 随机负载均衡器
 * @see ReactorServiceInstanceLoadBalancer 负载均衡器接口，选择服务器：{@link ReactorServiceInstanceLoadBalancer#choose(Request)}
 * @see RoundRobinLoadBalancer 轮询算法，选择服务器：{@link RoundRobinLoadBalancer#choose(Request)}
 * @see RandomLoadBalancer 随机算法，选择服务器：{@link RandomLoadBalancer#choose(Request)}
 * @see LoadBalancerClientConfiguration#reactorServiceInstanceLoadBalancer(Environment, LoadBalancerClientFactory) 默认为轮询算法
 * @see ServiceInstanceChooser 服务器示例选择器，根据服务名获取一个服务实例（{@link ServiceInstance}）。
 * @see org.springframework.cloud.client.loadbalancer.LoadBalancerClient 客户端负载均衡器，继承 {@link ServiceInstanceChooser}，会根据 {@link ServiceInstanceChooser} 和请求信息执行最终的结果
 * @see BlockingLoadBalancerClient 基于 Spring Cloud Starter LoadBalancer 的 {@link org.springframework.cloud.client.loadbalancer.LoadBalancerClient} 的默认实现。org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient 基于 Netflix Ribbon 的  {@link org.springframework.cloud.client.loadbalancer.LoadBalancerClient} 的实现
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#spring-cloud-loadbalancer">负载均衡器</a>
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#switching-between-the-load-balancing-algorithms">切换负载均衡算法</a>
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#spring-cloud-loadbalancer-integrations">负载均衡的集成</a>
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#zone-based-load-balancing">基于区域的负载平衡</a>
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#default-loadbalancer-cache-implementation">默认 LoadBalancer 缓存实现</a>
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#request-based-sticky-session-for-loadbalancer">基于请求的粘性会话</a>
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#hints-based-loadbalancing">基于提示的负载平衡</a>
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#transform-the-load-balanced-http-request">转换负载均衡的 HTTP 请求</a>
 * @see <a href="https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#loadbalancer-micrometer-stats-lifecycle">LoadBalancer 统计</a>
 * @since 0.0.1
 */
public class RandomLoadBalancerConfiguration {

    /**
     * 随机负载均衡器
     *
     * @param environment               环境
     * @param loadBalancerClientFactory 创建客户端、负载均衡器和客户端配置实例的工厂。 它为每个客户端名称创建一个 Spring ApplicationContext，并从那里提取它需要的 bean。
     * @return 随机负载均衡器 {@link Bean}
     */
    @Bean
    public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                                   LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        ObjectProvider<ServiceInstanceListSupplier> lazyProvider =
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class);
        return new RandomLoadBalancer(lazyProvider, name);
    }

}
