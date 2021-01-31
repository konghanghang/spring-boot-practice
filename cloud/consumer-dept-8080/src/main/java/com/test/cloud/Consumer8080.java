package com.test.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
// 如果不写@RibbonClient，mySelfRule放在scan可以扫描到的包下，则对所有服务生效
// @RibbonClient(name = "provider-dept", configuration = MySelfRule.class)
public class Consumer8080 {

    public static void main(String[] args) {
        SpringApplication.run(Consumer8080.class, args);
    }

}
