package com.test.juc.cp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
class ProducerAndConsumerLockTest {

    @Test
    void increment() {
        ProducerAndConsumerLock pcl = new ProducerAndConsumerLock();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    pcl.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    pcl.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
    }
}