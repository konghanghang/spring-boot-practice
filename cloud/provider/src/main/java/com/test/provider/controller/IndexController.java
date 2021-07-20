package com.test.provider.controller;

import com.iminling.core.annotation.EnableResolve;
import com.test.provider.service.BizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@EnableResolve
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

    @PostMapping("/circuit/{id}")
    public String circuit(@PathVariable("id") Integer id) {
        return bizService.bizCircuitBroker(id);
    }

    @PostMapping("/param")
    public String param(String name, String age){
        log.info("name:{}, age:{}", name, age);
        return "0k";
    }

    @PostMapping("/circuit/{id}/{aaa}")
    public String testParam(@PathVariable("id") Integer id, @PathVariable("aaa")String aaa, Object user) {
        log.info("id:{}, aaa:{}", id, aaa);
        return "ok";
    }

}
