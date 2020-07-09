package com.test.cloud2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan(value = {"com.test.cloud2.dao"})
@EnableEurekaClient
public class Provider8002 {

    public static void main(String[] args) {
        SpringApplication.run(Provider8002.class, args);
    }

}
