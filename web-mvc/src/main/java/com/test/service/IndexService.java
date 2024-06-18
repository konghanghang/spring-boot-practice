package com.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yslao@outlook.com
 * @since 2022/1/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    public String hello(String name) {
        return "hello " + name;
    }

}
