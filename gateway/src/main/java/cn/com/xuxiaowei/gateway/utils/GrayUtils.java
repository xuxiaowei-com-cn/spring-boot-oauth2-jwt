package cn.com.xuxiaowei.gateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 灰度 工具类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
public class GrayUtils {

    /**
     * 根据服务名选择服务（非灰度服务）
     *
     * @param instances 服务
     * @param serviceId 服务名
     * @return 返回服务
     */
    public static List<ServiceInstance> chooseServiceInstance(List<ServiceInstance> instances, String serviceId) {
        List<ServiceInstance> resultList = new ArrayList<>();

        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("没有可用于服务的服务：" + serviceId);
            }
            return resultList;
        }

        for (ServiceInstance serviceInstance : instances) {

            // 元数据
            Map<String, String> metadata = serviceInstance.getMetadata();

            String instanceId = serviceInstance.getInstanceId();
            if (instanceId == null) {
                instanceId = metadata.get("nacos.instanceId");
            }
            if (instanceId == null) {
                String host = serviceInstance.getHost();
                int port = serviceInstance.getPort();
                instanceId = String.format("%s：%s:%s", serviceId, host, port);
            }

            log.debug("服务：{}", instanceId);

            // 灰度发布的原数据
            String gray = metadata.get("gray");

            if (Boolean.TRUE.toString().equalsIgnoreCase(gray)) {
                // 示例为灰度

                log.debug("服务：{} 为灰度发布服务，在未来调用时可能跳过该服务", instanceId);

                // 灰度服务不添加
                continue;
            }

            resultList.add(serviceInstance);

        }

        return resultList;
    }

}
