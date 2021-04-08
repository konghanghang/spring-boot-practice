package com.test.juc.readwrite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
class MyCacheTest {

    @Test
    void write() {
        MyCache myCache = new MyCache();
        for (int i = 0; i < 6; i++) {
            int num = i;
            new Thread(() -> {
                myCache.write(num + "", num);
            }, i + "").start();
        }

        for (int i = 0; i < 6; i++) {
            int num = i;
            new Thread(() -> {
                myCache.read(num + "");
            }, i + "").start();
        }
    }
}