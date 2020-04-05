package com.test.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.test.annotation.Login;
import com.test.exception.AuthorizeException;
import com.test.model.LogModel;
import com.test.model.MessageCode;
import com.test.model.ThreadContext;
import com.test.utils.JwtUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

public class GlobalInterceptor implements HandlerInterceptor {

    private RequestDataReader requestDataReader;

    public GlobalInterceptor(RequestDataReader dataReader) {
        this.requestDataReader = dataReader;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequestDataWrapper dataWrapper;
        LogModel logModel = new LogModel();
        logModel.setUrl(request.getRequestURI());
        logModel.setAction(request.getHeader("action"));
        ThreadContext.setLogModel(logModel);
        if (requestDataReader.canRead(inputMessage)){
            dataWrapper = new RequestDataWrapper(true);
            JsonNode read = requestDataReader.read(inputMessage);
            dataWrapper.processParam(read);
            logModel.setRequestParams(read.toString());
        } else {
            String queryString = request.getQueryString();
            dataWrapper = new RequestDataWrapper(false);
            logModel.setRequestParams(queryString);
        }
        request.setAttribute(RequestArgumentResolver.DATA_KEY, dataWrapper);
        String token = request.getHeader("Authorization");
        LoginCheck(token, handlerMethod);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        if (!StringUtils.isEmpty(ThreadContext.getLogModel().getAction())){
            // action不为空，需要记录日志，具体实现在这里进行
            System.out.println("action不为空，日志记录");
            System.out.println(ThreadContext.getLogModel().toString());
        } else {
            System.out.println("action为空，不记录日志");
        }
        ThreadContext.clear();
    }

    private void LoginCheck(String token, HandlerMethod handlerMethod){
        Login loginRequired = getAnnotation(handlerMethod, Login.class);
        if (loginRequired != null) {
            if (StringUtils.isEmpty(token)) {
                throw new AuthorizeException(MessageCode.TOKEN_NULL);
            }
            String username = JwtUtil.getUsername(token);
            if (StringUtils.isEmpty(username)) {
                throw new AuthorizeException(MessageCode.TOKEN_ERROR);
            }
            if (!JwtUtil.verifyToken(token,username)) {
                throw new AuthorizeException(MessageCode.TOKEN_ERROR);
            }
            ThreadContext.getLogModel().setUsername(username);
        }
    }

    private  <T extends Annotation> T getAnnotation(HandlerMethod handlerMethod, Class<T> clazz) {
        T annotation = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), clazz);
        if (annotation == null) {
            annotation = handlerMethod.getBeanType().getAnnotation(clazz);
        }
        return annotation;
    }

}
