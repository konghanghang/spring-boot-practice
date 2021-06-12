package com.test.reflect;

import org.junit.jupiter.api.Test;


/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
class ClassUtilTest {

    @Test
    void test() {
        String s = "hello";
        ClassUtil.printClassMethodMessage(s);
        System.out.println("=============");
        ClassUtil.printClassFieldMessage(s);
        System.out.println("=============");
        ClassUtil.printConstructMessage(s);
    }

}