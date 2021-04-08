package com.test.juc.cp;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者和消费者,使用lock同步
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
public class ProducerAndConsumerLock {

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private int num = 0;

    public void increment() throws InterruptedException {
        lock.lock();
        try {
            // 这里的判断一定要使用while去判断
            while (num > 0) {
                condition.await();
            }
            num++;
            System.out.println(Thread.currentThread().getName() + "\t" + num);
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void decrement() throws InterruptedException {
        lock.lock();
        try {
            // 这里的判断一定要使用while去判断
            while (num == 0) {
                condition.await();
            }
            num--;
            System.out.println(Thread.currentThread().getName() + "\t" + num);
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

}
