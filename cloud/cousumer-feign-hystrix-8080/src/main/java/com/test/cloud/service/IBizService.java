package com.test.cloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "provider-hystrix")
public interface IBizService {

    @GetMapping("/hystrix/ok/{id}")
    public String ok(@PathVariable("id") String id);

    @GetMapping("/hystrix/time/{id}")
    public String time(@PathVariable("id") String id);

}
