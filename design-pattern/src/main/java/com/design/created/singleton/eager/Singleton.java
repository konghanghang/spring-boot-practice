package com.design.created.singleton.eager;

/**
 * 饿汉式
 */
public class Singleton {

    private static Singleton instance = new Singleton();
    private Singleton(){}
    public static Singleton getInstence(){
        return instance;
    }

}
