package com.test.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author yslao@outlook.com
 * @since 2022/8/31
 */
@FeignClient(name = "provider", contextId = "indexClient")
public interface IndexClient {

    @GetMapping("/index")
    Integer index();

}
