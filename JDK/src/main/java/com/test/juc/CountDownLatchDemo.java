package com.test.juc;

import java.util.concurrent.CountDownLatch;

/**
 * 等待都执行完毕再继续往下执行
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
public class CountDownLatchDemo {

    public void demo() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("main---------");
    }

}
