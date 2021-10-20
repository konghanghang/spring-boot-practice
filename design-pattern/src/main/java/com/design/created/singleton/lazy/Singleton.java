package com.design.created.singleton.lazy;

/**
 * 懒汉式
 */
public class Singleton {

    private static Singleton instance = null;
    private Singleton(){}
    synchronized public static Singleton getInstance(){
        if (instance == null)
            instance = new Singleton();
        return instance;
    }

}

/**
 * 我们无须对整个getInstance()方法进行锁定，只需对其中的代码“instance = new LazySingleton();”进行锁定即可
 * 仍然存在问题--
 * 可能同时存在多个线程进入==null判断里边
 */
class Singleton2{
    private static Singleton2 instance = null;
    private Singleton2(){}
    public static Singleton2 getInstance(){
        if (instance == null){
            synchronized (Singleton2.class){
                instance = new Singleton2();
            }
        }
        return instance;
    }
}

/**
 * 双重检查锁定(Double-Check Locking)
 */
class Singleton3{
    private volatile static Singleton3 instance = null;
    private Singleton3(){}
    public static Singleton3 getInstance(){
        if (instance == null){
            synchronized (Singleton3.class){
                if (instance == null)
                    instance = new Singleton3();
            }
        }
        return instance;
    }
}