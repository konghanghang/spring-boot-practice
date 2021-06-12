package com.test.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class ClassUtil {

    /**
     * 获取类的成员函数信息
     * @param obj
     */
    public static void printClassMethodMessage(Object obj) {
        Class c = obj.getClass();
        //获取类的名称
        System.out.println("类的名称是:"+c.getName());
        /**
         * Method类,方法对象
         * 一个成员方法就是一个Method对象
         * getMethods()方法获取的是所有的public的函数,包括父类继承而来的
         * getDeclaredMethods()获取该类自己自己声明的方法,不问访问权限
         */
        Method[] ms = c.getMethods();
        for (int i = 0; i < ms.length; i++) {
            Method m = ms[i];
            //得到方法的返回值类型的类类型
            Class returnType = m.getReturnType();
            System.out.print(returnType.getName() + " ");
            //得到方法名称
            System.out.print(m.getName() + "(");
            //获取参数类型-->得到参数列表的类型的类类型
            Class[] paramTypes = m.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++) {
                Class paramType = paramTypes[j];
                System.out.print(paramType.getName() + ",");
            }
            System.out.println(")");
        }



    }

    /**
     * 获取类的成员变量
     * @param obj
     */
    public static void printClassFieldMessage(Object obj) {

        Class c = obj.getClass();
        /**
         * 成员变量也是对象
         * java.lang.reflect.Field
         * Field类封装了关于成员变量的操作
         * getField()获取的是所有public的成员变量
         * getDeclaredFields()获取该类自己声明的成员变量信息
         */
        Field[] fs = c.getDeclaredFields();
        for (Field field : fs) {
            //得到成员变量的类型的类类型
            Class fieldType = field.getType();
            String typeName = fieldType.getName();
            //得到成员变量的名称
            String fieldName = field.getName();
            System.out.println(typeName + " " + fieldName);
        }
    }

    /**
     * 获取对象的构造函数
     * @param obj
     */
    public static void printConstructMessage(Object obj){
        Class c = obj.getClass();
        /**
         * 构造函数也是对象
         * java.lang.constructor中封装了构造函数的信息
         * getConstructors()获取所有public的构造函数
         * getDeclaredConstructors()获取所有的构造函数
         */
        Constructor[] cs = c.getDeclaredConstructors();
        for (Constructor constructor : cs) {
            System.out.print(constructor.getName() + "(");
            //获取构造函数的参数列表-->得到参数列表的类类型
            Class[] paramTypes = constructor.getParameterTypes();
            for (Class paramType : paramTypes) {
                System.out.print(paramType.getName() + ",");
            }
            System.out.println(")");
        }
    }

}
