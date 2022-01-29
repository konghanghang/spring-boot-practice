package com.test.service.impl;

import com.test.service.IndexService;
import org.springframework.stereotype.Service;

/**
 * @author yslao@outlook.com
 * @since 2022/1/29
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
