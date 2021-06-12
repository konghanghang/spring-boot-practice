package com.test.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
class SemaphoreDemoTest {

    @Test
    void demo() throws InterruptedException {
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        semaphoreDemo.demo();
        TimeUnit.SECONDS.sleep(10);
    }
}