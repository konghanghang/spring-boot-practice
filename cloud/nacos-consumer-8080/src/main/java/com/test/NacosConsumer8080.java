package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosConsumer8080 {

    public static void main(String[] args) {
        SpringApplication.run(NacosConsumer8080.class, args);
    }

}
