package com.test.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallController {

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/nacos/{id}")
    public String nacos(@PathVariable Long id){
        return "nacos registry, server port:" + port + ", id = " + id;
    }

}
