package com.test.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.model.MessageCode;
import com.test.model.ResultModel;
import com.test.model.ThreadContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestControllerAdvice
public class GlobalReturnValueHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> parameterType = returnType.getParameterType();
        if (ResponseEntity.class.isAssignableFrom(parameterType)
                || StreamingResponseBody.class.isAssignableFrom(parameterType)
                || SseEmitter.class.isAssignableFrom(parameterType)){
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ResultModel) {
            ThreadContext.getLogModel().setResult(JSON.toJSONString(body));
            return body;
        }
        ResultModel resultModel = new ResultModel(MessageCode.RESULT_OK);
        if (isPrimitiveOrVoid(returnType.getParameterType())) {
            JSONObject json = new JSONObject();
            json.put("result", body);
            body = json;
        }
        resultModel.setData(body);
        String result = JSON.toJSONString(resultModel);
        ThreadContext.getLogModel().setResult(result);
        boolean isString = returnType.getParameterType() == String.class;
        // 防止方法返回类型是string的时候报错
        if (isString) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
            return result;
        }
        return resultModel;
    }

    private boolean isPrimitiveOrVoid(Class<?> returnType) {
        return returnType.isPrimitive()
                || Number.class.isAssignableFrom(returnType)
                || CharSequence.class.isAssignableFrom(returnType)
                || Character.class.isAssignableFrom(returnType)
                || Boolean.class.isAssignableFrom(returnType);
    }
}
