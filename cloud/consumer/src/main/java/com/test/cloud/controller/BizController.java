package com.test.cloud.controller;

import com.test.cloud.feign.IBizCustomizeService;
import com.test.cloud.feign.IBizService;
import com.test.cloud.model.User;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
//@RefreshScope
//@EnableResolve
//@RestController
//@RequestMapping("/consumer")
//@DefaultProperties(defaultFallback = "globalHandler")
public class BizController {

    @Resource
    private IBizService bizService;
    @Resource
    private IBizCustomizeService bizCustomizeService;
    @Value("${info.name}")
    private String name;

    @GetMapping("/ok/{id}")
    public String ok(@PathVariable("id") String id) {
        // 通过MDC机制在日志中添加需要加入的东西 在logback的日志模式中用${traceId}取出即可
        /*MDC.put("traceId", UUID.randomUUID().toString());*/
        log.info("name:{}", name);
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
    public String time(@PathVariable("id") String id) {
        if ("-1".equals(id)) {
            throw new RuntimeException("-1");
        }
        return bizService.time(id);
    }

    /**
     * 全局Fallback，需要标注@HystrixCommand注解，并且类上需要有
     * @DefaultProperties(defaultFallback = "globalHandler")注解
     * 才会在发生异常的时候使用默认的fallback
     * @param id
     * @return
     */
    @GetMapping("/circuit/{id}")
    public String circuit(@PathVariable("id") Integer id) {
        if (id == -1) {
            throw new RuntimeException("-1");
        }
        return bizService.circuit(id);
    }

    @GetMapping("/param")
    public String param(String name, String age){
        //bizService.body(new User().setName(name).setAge(Integer.valueOf(age)));
        // bizService.testParam(4, "xxx");
        String s = bizCustomizeService.testParam(4, "xxx", new User().setAge(10).setName("zhangsan"));
        bizService.param(name, age);
        return s;
    }

}
