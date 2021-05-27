package com.test.cloud.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule() {
        // Ribbon默认是轮询，Random为随机
        return new RoundRobinRule();
        // new RandomRule();
        //return new MyRule();
    }

}
