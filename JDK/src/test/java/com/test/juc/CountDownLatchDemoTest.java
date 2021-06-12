package com.test.juc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
class CountDownLatchDemoTest {

    @Test
    void demo() throws InterruptedException {
        CountDownLatchDemo downLatchDemo = new CountDownLatchDemo();
        downLatchDemo.demo();
    }
}