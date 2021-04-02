package com.test.controller;

import com.test.client.UserFeignClient;
import com.test.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConsumerController {

    @Value("${config.info:}")
    private String info;

    @Autowired
    private UserFeignClient userFeignClient;

    /*@Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consumer/nacos/{id}")
    public String nacos(@PathVariable Long id) {
        String url = "http://nacos-provider/nacos/" + id;
        return restTemplate.getForObject(url, String.class);
    }*/

    @GetMapping("/consumer/nacos/feign")
    public String nacos() {
        Object test = userFeignClient.getUser(new User());
        return "url";
    }
}
