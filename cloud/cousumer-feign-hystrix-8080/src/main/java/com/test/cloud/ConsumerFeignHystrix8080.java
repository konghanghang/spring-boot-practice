package com.test.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.test.cloud")
@EnableHystrix
public class ConsumerFeignHystrix8080 {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerFeignHystrix8080.class, args);
    }

}
