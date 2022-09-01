package com.test.provider.controller;

import com.iminling.core.annotation.EnableResolve;
import com.test.api.client.IndexClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EnableResolve
@RestController
public class IndexController implements IndexClient {

    @Value("${server.port}")
    private Integer serverPort;

    @Override
    public Integer index() {
        return serverPort;
    }

}
