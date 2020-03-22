package com.test.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class GlobalInterceptor implements HandlerInterceptor {

    @Resource
    private RequestDataReader requestDataReader;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
        // HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequestDataWrapper dataWrapper;
        if (requestDataReader.canRead(inputMessage)){
            dataWrapper = new RequestDataWrapper(true);
            JsonNode read = requestDataReader.read(inputMessage);
            dataWrapper.processParam(read);
        } else {
            dataWrapper = new RequestDataWrapper(false);
        }
        request.setAttribute(RequestArgumentResolver.DATA_KEY, dataWrapper);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
    }

}
