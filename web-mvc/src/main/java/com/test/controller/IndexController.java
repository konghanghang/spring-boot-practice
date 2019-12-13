package com.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class IndexController {

    @RequestMapping("/index")
    public String index(String name) {
        System.out.println("index controller");
        return "index";
    }

}
