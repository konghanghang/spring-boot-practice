package com.test.client;

import com.test.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yslao@outlook.com
 * @since 2021/4/2
 */
@FeignClient(name = "nacos-provider")
public interface UserFeignClient {

    @RequestMapping(value = "/111", consumes = "application/json")
    Object getUser(User user);

}
