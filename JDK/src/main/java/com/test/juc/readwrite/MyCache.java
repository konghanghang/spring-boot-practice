package com.test.juc.readwrite;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
public class MyCache {

    private Map<String, Object> cache = new HashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void write(String key, Object value) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t开始写入");
            cache.put(key, value);
            System.out.println(Thread.currentThread().getName() + "\t写入完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void read(String key) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t开始读取");
            Object o = cache.get(key);
            System.out.println(Thread.currentThread().getName() + "\t读取完成:" + o);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

}
