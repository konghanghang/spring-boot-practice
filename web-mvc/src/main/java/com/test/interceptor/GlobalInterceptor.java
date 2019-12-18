package com.test.interceptor;

import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class GlobalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        if ("/error".equalsIgnoreCase(requestURI)) return true;
        System.out.println("preHandler...");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request);
        InputStream inputStream = servletServerHttpRequest.getBody();
        System.out.println(inputStream);
        System.out.println(request.getQueryString());
        String method = request.getMethod();
        System.out.println(method);
        InputStream body;
        if (inputStream.markSupported()){
            System.out.println("----");
            inputStream.mark(1);
            body = (inputStream.read() != -1) ? inputStream : null;
        } else {
            PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
            int read = pushbackInputStream.read();
            if (read == -1){
                System.out.println("param is null");
                body = null;
            } else {
                body = pushbackInputStream;
                pushbackInputStream.unread(read);
            }
        }
        if (body != null) {

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                            @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 @Nullable Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        if ("/error".equalsIgnoreCase(requestURI)) return;
        System.out.println("afterCompletion...");
    }

}
