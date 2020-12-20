package com.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

@Aspect//告诉spring这是一个切面
public class LogAspects {

    @Pointcut("execution(public int com.aop.MathCalculator.*(..))")
    public void pointCut(){}

    @Before("execution(public int com.aop.MathCalculator.div(int, int))")
    public void logStart(JoinPoint joinPoint){
        final Object[] args = joinPoint.getArgs();
        System.out.println(joinPoint.getSignature().getName() + "运行，参数列表：" + Arrays.asList(args));
    }

    /**
     * 无论方法是正常结束还是异常结束
     */
    @After("execution(public int com.aop.MathCalculator.*(..))")
    public void logEnd(JoinPoint joinPoint){
        System.out.println(joinPoint.getSignature().getName() + "结束。。。。");
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void logReturn(JoinPoint joinPoint, Object result){
        System.out.println(joinPoint.getSignature().getName() + "正常返回，运行结果:" + result);
    }

    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception){
        System.out.println(joinPoint.getSignature().getName() + "除法异常，异常信息：" + exception.getMessage());
    }

}
