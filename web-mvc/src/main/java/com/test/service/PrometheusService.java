package com.test.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrometheusService {

    private final MeterRegistry meterRegistry;

    public void request() {
        try {
            this.counter("all", "request");
            //业务逻辑
            this.counter("success", "request");
        } catch (Exception e) {
            this.counter("error", "request");
        }
    }

    public void request2() {
        try {
            this.counter("all", "request2");
            //业务逻辑
            this.counter("success", "request2");
        } catch (Exception e) {
            this.counter("error", "request2");
        }
    }

    private void counter(String nameType, String methodName) {
        try {

            String name = "";
            String desc = "";
            if ("all".equals(nameType)) {
                name = "callback.request";
                desc = "TPS for callback requests";
            } else if ("error".equals(nameType)) {
                name = "callback.request.error";
                desc = "ERROR for callback requests";
            } else if ("success".equals(nameType)) {
                name = "callback.request.success";
                desc = "SUCCESS for callback requests";
            }
            Counter.builder(name)
                .tag("methodName", methodName)
                //.tag("xxx", xxx)
                //.tag("xxx2", xxx2)
                .description(desc)
                .register(meterRegistry)
                .increment();
        } catch (Exception e) {
            log.error("counter error, nameType:{}", nameType);
        }
    }

}
