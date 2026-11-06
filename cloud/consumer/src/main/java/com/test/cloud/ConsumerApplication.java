package com.test.cloud;

import com.test.cloud.config.feign.FeignClientConfiguration;
import  org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author yslao@outlook.com
 * @since 2021/5/26
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.test.api.client", defaultConfiguration = FeignClientConfiguration.class)
@EnableDiscoveryClient
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
