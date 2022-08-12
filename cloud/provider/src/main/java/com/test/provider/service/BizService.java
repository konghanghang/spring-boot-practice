package com.test.provider.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class BizService {

    public String bizOk(String id) {
        return Thread.currentThread().getName() + "_provider_bizOk id:" + id;
    }

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
