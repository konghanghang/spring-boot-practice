package com.test.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 7个任务都执行了,再输出cyclicBarrier 执行
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
public class CyclicBarrierDemo {

    public void demo() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> System.out.println("cyclicBarrier 执行"));
        for (int i = 0; i < 7; i++) {
            int num = i;
            new Thread(() -> {
                System.out.println("now num: " + num);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, i + "").start();
        }
    }

}
