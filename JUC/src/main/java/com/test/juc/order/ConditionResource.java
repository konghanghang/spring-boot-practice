package com.test.juc.order;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程间精准唤醒执行
 * A执行完成唤醒B
 * B执行完成唤醒C
 * C执行完成唤醒A
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
public class ConditionResource {

    private int flag = 1;
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    public void print2() {
        lock.lock();
        try {
            // 判断
            while (flag != 1) {
                condition1.await();
            }
            // 执行
            for (int i = 1; i <= 2; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            // 唤醒
            flag = 2;
            condition2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print3() {
        lock.lock();
        try {
            // 判断
            while (flag != 2) {
                condition2.await();
            }
            // 执行
            for (int i = 1; i <= 3; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            // 唤醒
            flag = 3;
            condition3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print5() {
        lock.lock();
        try {
            // 判断
            while (flag != 3) {
                condition3.await();
            }
            // 执行
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            // 唤醒
            flag = 1;
            condition1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
