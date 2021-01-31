package com.test.cloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 加上fallback，处理服务提供者宕机时返回对应异常
 */
@FeignClient(value = "provider-hystrix", fallback = BizNewService.class)
public interface IBizService {

    @GetMapping("/hystrix/ok/{id}")
    public String ok(@PathVariable("id") String id);

    @GetMapping("/hystrix/time/{id}")
    public String time(@PathVariable("id") String id);

}
