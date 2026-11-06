package com.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolsApplication.class, args);
    }

}
