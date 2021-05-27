package com.test.cloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.test.cloud.feign.IBizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/consumer")
@DefaultProperties(defaultFallback = "globalHandler")
public class BizController {

    @Resource
    private IBizService bizService;

    @GetMapping("/ok/{id}")
    public String ok(@PathVariable("id") String id) {
        return bizService.ok(id);
    }

    /**
     * 降级处理，如果调用的服务出现异常，则会走bizService的熔断机制
     * 如果调用服务时间超过这里配置的1.5s，则进行自定义的fallback方法
     * 如果@HystrixCommand指定了fallbackMethod，则出现异常时也是走这个方法
     * 不管异常符不符合commandProperties中的定义
     * @param id
     * @return
     */
    @GetMapping("/time/{id}")
    @HystrixCommand(fallbackMethod="bizTimeHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
    })
    public String time(@PathVariable("id") String id) {
        if ("-1".equals(id)) {
            throw new RuntimeException("-1");
        }
        return bizService.time(id);
    }
    public String bizTimeHandler(@PathVariable("id") String id) {
        return "我是消费者，服务提供者出现问题。";
    }

    /**
     * 全局Fallback，需要标注@HystrixCommand注解，并且类上需要有
     * @DefaultProperties(defaultFallback = "globalHandler")注解
     * 才会在发生异常的时候使用默认的fallback
     * @param id
     * @return
     */
    @GetMapping("/circuit/{id}")
    @HystrixCommand
    public String circuit(@PathVariable("id") Integer id) {
        if (id == -1) {
            throw new RuntimeException("-1");
        }
        return bizService.circuit(id);
    }

    public String globalHandler() {
        return "我是global全局降级方法。";
    }

}
