package com.test.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

/**
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
class CompletableFutureDemoTest {

    @Test
    void demo() throws ExecutionException, InterruptedException {

        CompletableFutureDemo completableFutureDemo = new CompletableFutureDemo();
        completableFutureDemo.demo();

    }
}