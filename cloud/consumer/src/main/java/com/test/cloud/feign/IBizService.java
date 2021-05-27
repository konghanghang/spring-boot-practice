package com.test.cloud.feign;

import com.test.cloud.feign.fallback.BizNewService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 加上fallback，处理服务提供者宕机时返回对应异常
 * nacos对client名称区分大小写，eureka不区分
 */
@Component
@FeignClient(value = "provider", fallback = BizNewService.class)
public interface IBizService {

    @GetMapping("/hystrix/ok/{id}")
    String ok(@PathVariable("id") String id);

    @GetMapping("/hystrix/time/{id}")
    String time(@PathVariable("id") String id);

    @GetMapping("/hystrix/circuit/{id}")
    String circuit(@PathVariable("id") Integer id);

}
