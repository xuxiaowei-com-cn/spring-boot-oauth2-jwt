package cn.com.xuxiaowei.gateway;

import cn.com.xuxiaowei.gateway.annotation.EnableRandomLoadBalancer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRandomLoadBalancer
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
