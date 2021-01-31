package com.test.cloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.test.cloud.service.IBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer/common")
@DefaultProperties(defaultFallback = "globalHandler")
public class BizCommonController {

    @Autowired
    private IBizService bizService;

    @GetMapping("/ok/{id}")
    public String ok(@PathVariable("id") String id) {
        return bizService.ok(id);
    }

    @GetMapping("/time/{id}")
    @HystrixCommand
    public String time(@PathVariable("id") String id) {
        int i = 10 / 0;
        return bizService.time(id);
    }


    public String globalHandler() {
        return "我是global全局降级方法。";
    }

}
