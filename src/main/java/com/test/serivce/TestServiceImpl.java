package com.test.serivce;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestServiceImpl implements ITestService {
    @Override
    public void time() {
        System.out.println(LocalDateTime.now());
    }
}
