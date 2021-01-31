package com.test.hystrix.cloud1.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BizService {

    public String bizOk(String id) {
        return Thread.currentThread().getName() + "_bizOk id:" + id;
    }

    @HystrixCommand(fallbackMethod="bizTimeHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public String bizTime(String id) {
        int i = 10/0;
        int timeNum = 5;
        try {
            TimeUnit.SECONDS.sleep(timeNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Thread.currentThread().getName() + "_bizTime id:" + id + "\t 耗时：" + timeNum ;
    }

    public String bizTimeHandler(String id) {
        return Thread.currentThread().getName() + "bizTimeHandler 8001系统繁忙或运行错误稍后再试 id:" + id;
    }

}
