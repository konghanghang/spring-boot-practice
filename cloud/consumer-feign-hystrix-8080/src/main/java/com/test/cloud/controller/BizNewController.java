package com.test.cloud.controller;

import com.test.cloud.service.IBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer/new")
public class BizNewController {

    @Autowired
    private IBizService bizService;

    @GetMapping("/ok/{id}")
    public String ok(@PathVariable("id") String id) {
        return bizService.ok(id);
    }

    @GetMapping("/time/{id}")
    public String time(@PathVariable("id") String id) {
        return bizService.time(id);
    }

}
