package com.test.juc.ticket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
class TicketLockTest {

    @Test
    void sale() {
        TicketLock ticketLock = new TicketLock();
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticketLock.sale();
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticketLock.sale();
            }
        }, "B").start();
    }
}