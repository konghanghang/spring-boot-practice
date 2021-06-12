package com.test.juc.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A->B->C
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
class ConditionResourceTest {

    @Test
    void print2() {
        ConditionResource conditionResource = new ConditionResource();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                conditionResource.print2();
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                conditionResource.print3();
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                conditionResource.print5();
            }
        }, "C").start();
    }
}