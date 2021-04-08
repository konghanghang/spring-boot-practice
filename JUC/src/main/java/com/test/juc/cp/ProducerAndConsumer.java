package com.test.juc.cp;

/**
 * 生产者和消费者,使用synchronized同步
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
public class ProducerAndConsumer {

    private int num = 0;

    public synchronized void increment() throws InterruptedException {
        // 这里的判断一定要使用while去判断
        while (num > 0) {
            this.wait();
        }
        num++;
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        this.notifyAll();
    }

    public synchronized void decrement() throws InterruptedException {
        // 这里的判断一定要使用while去判断
        while (num == 0) {
            this.wait();
        }
        num--;
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        this.notifyAll();
    }

}
