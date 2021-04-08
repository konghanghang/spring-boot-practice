package com.test.juc.ticket;

import org.junit.jupiter.api.Test;

/**
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
class TicketSynchronizedTest {

    @Test
    void sale() {

        TicketSynchronized ticket = new TicketSynchronized();
        new Thread(() -> {
            for (int i = 0; i < 12; i++) {
                ticket.sale();
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "C").start();

    }
}