package com.test.provider.controller;

import com.test.provider.service.BizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/hystrix")
public class IndexController {

    @Resource
    private BizService bizService;

    @GetMapping("/ok/{id}")
    public String ok(@PathVariable("id") String id) {
        log.info("请求id:{}", id);
        return bizService.bizOk(id);
    }

    @GetMapping("/time/{id}")
    public String time(@PathVariable("id") String id) {
        return bizService.bizTime(id);
    }

    @GetMapping("/circuit/{id}")
    public String circuit(@PathVariable("id") Integer id) {
        return bizService.bizCircuitBroker(id);
    }

}
