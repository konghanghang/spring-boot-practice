package com.test.cloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.test.cloud.service.IBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
public class BizController {

    @Autowired
    private IBizService bizService;

    @GetMapping("/ok/{id}")
    public String ok(@PathVariable("id") String id) {
        return bizService.ok(id);
    }

    @GetMapping("/time/{id}")
    @HystrixCommand(fallbackMethod="bizTimeHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
    })
    public String time(@PathVariable("id") String id) {
        return bizService.time(id);
    }
    public String bizTimeHandler(@PathVariable("id") String id) {
        return "我是消费者，服务提供者出现问题。";
    }

}
