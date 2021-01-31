package com.test.cloud.config;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule() {
        // Ribbon默认是轮询，我自定义为随机
        // new RoundRobinRule();
        // new RandomRule();
        return new MyRule();
    }

}
