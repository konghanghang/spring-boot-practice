package com.test.reflect;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class ClassDemo1 {
    public static void main(String[] args) {
        //Foo的实列对象表示
        Foo foo1 = new Foo();
        //Foo这个类也是一个实列对象,class类的实列对象
        //任何一个类都是Class的实列对象,这个实列对象有三种表示方式

        //一丶实际是在告诉我们任何一个类都有一个隐含的静态成员变量class
        Class c1 = Foo.class;

        //二丶已知该类的对象通过getClass方法
        Class c2 = foo1.getClass();

        /**
         * c1,c2表示了Foo类的类类型(class type)
         * 类也是对象,是Class类的实列对象
         * 这个对象我们称为该类的类类型
         */

        //不管c1 c2 都代表了Foo类的类类型,一个类只可能是Class类的一个实列对象
        System.out.println(c1 == c2);

        //三丶
        Class c3 = null;
        try {
            c3 = Class.forName("com.ysla.reflect.Foo");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(c2 == c3);

        //可以通过类的类类型对象创建该类的对象实列
        try {
            //需要有无参数的构造函数
            Foo foo = (Foo) c1.newInstance();
            foo.print();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

class Foo{
    void print(){
        System.out.println("foo");
    }
}
