package com.test.juc.ticket;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 卖票,使用Lock同步
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
public class TicketLock {

    private Lock lock = new ReentrantLock();

    private int num = 30;

    public void sale() {
        lock.lock();
        try {
            if (num > 0) {
                System.out.println(Thread.currentThread().getName() + "\t卖出了第" + (num--) + ",还剩" + num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

}
