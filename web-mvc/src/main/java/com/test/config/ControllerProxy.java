package com.test.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@Aspect
public class ControllerProxy {

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void executor(JoinPoint joinPoint) {
        System.out.println("before controller method");
        Object[] args = joinPoint.getArgs();
        int length = args.length;
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        System.out.println(request.getRequestURL());
        System.out.println(request.getRequestURI());
        //HttpServletRequest requests = (HttpServletRequest) args[length - 2];
        //HttpServletResponse response = (HttpServletResponse) args[length - 1];
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object target = joinPoint.getTarget();
    }

}
