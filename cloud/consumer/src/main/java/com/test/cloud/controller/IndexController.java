package com.test.cloud.controller;

import com.test.api.client.IndexClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yslao@outlook.com
 * @since 2022/8/31
 */
@RequiredArgsConstructor
@RestController
public class IndexController {

    private final IndexClient indexClient;

    @RequestMapping("/index")
    public Integer index() {
        return indexClient.index();
    }
}
