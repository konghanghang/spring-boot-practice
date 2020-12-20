package com.aop;

import com.aop.config.MainConfigOfAop;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopTest {

    @Test
    public void test01(){
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfigOfAop.class);
        final MathCalculator bean = context.getBean(MathCalculator.class);
        final int div = bean.div(10, 5);
    }

}
