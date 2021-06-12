package com.test.juc.ticket;

/**
 * 卖票,使用synchronized同步
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
public class TicketSynchronized {

    private int num = 30;

    public synchronized void sale() {
        if (num > 0) {
            System.out.println(Thread.currentThread().getName() + "\t卖出了第" + (num--) + ",还剩" + num);
        }
    }

}
