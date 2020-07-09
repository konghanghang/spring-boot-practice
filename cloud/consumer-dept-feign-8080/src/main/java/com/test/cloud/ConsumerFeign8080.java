package com.test.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.test.cloud")
public class ConsumerFeign8080 {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerFeign8080.class, args);
    }

}
