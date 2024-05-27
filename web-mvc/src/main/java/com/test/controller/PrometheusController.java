package com.test.controller;

import com.test.service.PrometheusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PrometheusController {

    private final PrometheusService prometheusService;

    public void test() {
        prometheusService.request();
    }

}
