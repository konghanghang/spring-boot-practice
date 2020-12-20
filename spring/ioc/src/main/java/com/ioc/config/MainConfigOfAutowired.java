package com.ioc.config;

import com.ioc.bean.Yellow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(value = {"com.ioc.controller", "com.ioc.service", "com.ioc.dao"})
@Configuration
public class MainConfigOfAutowired {

    @Bean
    public Yellow yellow(){
        return new Yellow();
    }

}
