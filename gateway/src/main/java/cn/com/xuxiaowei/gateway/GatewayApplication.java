package cn.com.xuxiaowei.gateway;

import cn.com.xuxiaowei.gateway.annotation.EnableGrayRandomLoadBalancer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableGrayRandomLoadBalancer
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
