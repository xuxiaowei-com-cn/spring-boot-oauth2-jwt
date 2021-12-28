package cn.com.xuxiaowei.gateway.configuration;

import cn.com.xuxiaowei.gateway.utils.GrayUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.reactive.Request;
import org.springframework.cloud.client.loadbalancer.reactive.Response;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 灰度 随机负载均衡器 配置
 * <p>
 * 使用：如：{@link RandomLoadBalancerConfiguration}
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class GrayRandomLoadBalancerConfiguration {

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
        return new GrayRandomLoadBalancer(lazyProvider, name);
    }

    /**
     * 灰度 随机负载均衡器
     *
     * @author xuxiaowei
     * @since 0.0.1
     */
    @Slf4j
    static class GrayRandomLoadBalancer extends RandomLoadBalancer {

        private final String serviceId;

        @Deprecated
        private ObjectProvider<ServiceInstanceSupplier> serviceInstanceSupplier;

        private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

        public GrayRandomLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
            super(serviceInstanceListSupplierProvider, serviceId);
            this.serviceId = serviceId;
            this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        }

        public GrayRandomLoadBalancer(String serviceId, ObjectProvider<ServiceInstanceSupplier> serviceInstanceSupplier) {
            super(serviceId, serviceInstanceSupplier);
            this.serviceId = serviceId;
            this.serviceInstanceSupplier = serviceInstanceSupplier;
        }

        @Override
        public Mono<Response<ServiceInstance>> choose(
                Request request) {
            // TODO: move supplier to Request?
            // Temporary conditional logic till deprecated members are removed.
            if (serviceInstanceListSupplierProvider != null) {
                ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                        .getIfAvailable(NoopServiceInstanceListSupplier::new);
                return supplier.get().next()
                        .map(serviceInstances -> processInstanceResponse(supplier,
                                serviceInstances));
            }
            ServiceInstanceSupplier supplier = this.serviceInstanceSupplier
                    .getIfAvailable(NoopServiceInstanceSupplier::new);
            return supplier.get().collectList().map(this::getInstanceResponse);
        }

        private Response<ServiceInstance> processInstanceResponse(
                ServiceInstanceListSupplier supplier,
                List<ServiceInstance> serviceInstances) {
            Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(
                    serviceInstances);
            if (supplier instanceof SelectedInstanceCallback
                    && serviceInstanceResponse.hasServer()) {
                ((SelectedInstanceCallback) supplier)
                        .selectedServiceInstance(serviceInstanceResponse.getServer());
            }
            return serviceInstanceResponse;
        }

        /**
         * 执行选择
         */
        private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {

            List<ServiceInstance> resultList = GrayUtils.chooseServiceInstance(instances, serviceId);

            int resultSize = resultList.size();

            ServiceInstance instance;

            if (resultSize == 0) {
                log.warn("服务全部为灰度，返回所有服务器");

                int index = ThreadLocalRandom.current().nextInt(instances.size());

                instance = instances.get(index);

            } else {
                int size = instances.size();

                log.debug("服务数量：{}，正常服务：{} 灰度服务：{}", size, resultSize, size - resultSize);

                int index = ThreadLocalRandom.current().nextInt(resultList.size());

                instance = resultList.get(index);
            }

            return new DefaultResponse(instance);
        }

    }

}
