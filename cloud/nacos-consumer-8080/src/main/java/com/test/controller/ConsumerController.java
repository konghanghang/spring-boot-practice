package com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consumer/nacos/{id}")
    public String nacos(@PathVariable Long id) {
        String url = "http://nacos-provider/nacos/" + id;
        return restTemplate.getForObject(url, String.class);
    }

}
