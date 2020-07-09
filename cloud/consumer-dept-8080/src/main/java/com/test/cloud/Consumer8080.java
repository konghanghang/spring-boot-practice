package com.test.cloud;

import com.test.cloud.config.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "provider-dept", configuration = MySelfRule.class)
public class Consumer8080 {

    public static void main(String[] args) {
        SpringApplication.run(Consumer8080.class, args);
    }

}
