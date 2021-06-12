package com.test.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class MethodDemo {

    public static void demo1() {
        A a1 = new A();
        Class c = a1.getClass();
        /**
         * 获取方法,名称和参数列表来决定
         * getMethod获取public方法
         * getDeclaredMethod自己声明的方法
         */
        try {
            //获取方法对象
            //Method method = c.getMethod("print",new Class[]{int.class,int.class})
            Method method = c.getMethod("print", int.class, int.class);
            //方法的反射操作
            //a1.print(10,20),方法的反射操作使用method对象来进行方法调用和a1.print()调用效果一样
            method.invoke(a1,new Object[]{10,20});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void demo2() {
        ArrayList list = new ArrayList();
        ArrayList<String> list1 = new ArrayList<String>();


        Class c1 = list.getClass();
        Class c2 = list1.getClass();

        System.out.println(c1 == c2);

        //反射操作都是编译后的操作

        /**
         * c1 == c2结果返回true说明编译后集合的泛型是去泛型化的
         * java中集合的泛型,是防止错误输入的,只在编译阶段有效
         * 绕过编译就无效了
         * 验证:通过方法的反射来操作,绕过编译
         */
        try {
            Method method = c1.getMethod("add", Object.class);
            method.invoke(list,10);
            System.out.println(list.size());
            System.out.println(list);
            //现在不能这样遍历,会有类型转换异常
            /*for (String s : list1) {
                System.out.println(s);
            }*/
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}

class A{
    public void print(int a, int b){
        System.out.println(a + b);
    }
    public void print(String a, String b){
        System.out.println(a.toUpperCase() + "," + b.toLowerCase());
    }
}