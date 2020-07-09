package com.test.cloud3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan(value = {"com.test.cloud3.dao"})
@EnableEurekaClient
public class Provider8003 {

    public static void main(String[] args) {
        SpringApplication.run(Provider8003.class, args);
    }

}
