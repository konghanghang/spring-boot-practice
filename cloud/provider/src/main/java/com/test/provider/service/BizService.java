package com.test.provider.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class BizService {

    public String bizOk(String id) {
        return Thread.currentThread().getName() + "_provider_bizOk id:" + id;
    }

    @HystrixCommand(fallbackMethod="bizTimeHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public String bizTime(String id) {
        int timeNum = 5;
        try {
            TimeUnit.SECONDS.sleep(timeNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Thread.currentThread().getName() + "_provider_bizTime id:" + id + "\t 耗时：" + timeNum ;
    }

    public String bizTimeHandler(String id) {
        return Thread.currentThread().getName() + "bizTimeHandler 8001系统繁忙或运行错误稍后再试 id:" + id;
    }

    @HystrixCommand(fallbackMethod = "bizCircuitBrokerHandler", commandProperties = {
            // 具体属性值见com.netflix.hystrix.HystrixCommandProperties
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "6")//失败率达到多少开始跳闸
    })
    public String bizCircuitBroker(Integer id) {
        if (id < 0) {
            throw new RuntimeException("----id不能为负");
        }
        return Thread.currentThread().getName() + "调用成功：" + UUID.randomUUID().toString();
    }

    public String bizCircuitBrokerHandler(Integer id) {
        return "----id不能为负, id:" + id;
    }

}
