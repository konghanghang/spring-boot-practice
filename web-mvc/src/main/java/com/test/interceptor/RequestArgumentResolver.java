package com.test.interceptor;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.*;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

public class RequestArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String DATA_KEY = RequestArgumentResolver.class.getName() + ".myData";

    private final Logger logger = LoggerFactory.getLogger(RequestArgumentResolver.class);

    private ObjectMapper objectMapper;

    public RequestArgumentResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        RequestDataWrapper requestDataWrapper = (RequestDataWrapper) request.getAttribute(DATA_KEY);
        return requestDataWrapper != null && requestDataWrapper.isCanRead();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Assert.state(mavContainer != null, "ModelAttributeMethodProcessor requires ModelAndViewContainer");
        Assert.state(binderFactory != null, "ModelAttributeMethodProcessor requires WebDataBinderFactory");

        RequestDataWrapper requestDataWrapper = null;
        if (webRequest != null) {
            requestDataWrapper = (RequestDataWrapper) webRequest.getAttribute(DATA_KEY, RequestAttributes.SCOPE_REQUEST);
        }
        if (requestDataWrapper == null) {
            String path = null;
            if (webRequest instanceof ServletWebRequest) {
                path = ((ServletWebRequest)webRequest).getRequest().getServletPath();
            }
            throw new IllegalArgumentException(String.format("参数'%s'处理异常,path = %s",
                    parameter.getParameterName(), path != null ? path : parameter.getMethod()));
        }
        parameter = parameter.nestedIfOptional();
        Object arg = readWithRequestData(requestDataWrapper, parameter, parameter.getNestedGenericParameterType());
        arg = handleNullValue(parameter.getParameterName(), arg, parameter.getParameterType());
        return adaptArgumentIfNecessary(arg, parameter);
    }

    protected Object readWithRequestData(RequestDataWrapper requestDataWrapper, MethodParameter parameter, Type parameterType) throws Exception {
        Object result = null;
        if (requestDataWrapper.isHashParams()) {
            JsonNode params = requestDataWrapper.getParams();
            String parameterName = parameter.getParameterName();
            if (params.hasNonNull(parameterName)) {
                JavaType javaType = getJavaType(parameterType, null);
                JsonNode value = params.path(parameterName);
                // 这里可能还需要捕捉异常
                result = objectMapper.convertValue(value, javaType);
            }
        }
        return result;
    }

    protected JavaType getJavaType(Type type, @Nullable Class<?> contextClass) {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return typeFactory.constructType(GenericTypeResolver.resolveType(type, contextClass));
    }

    private Object handleNullValue(String name, @Nullable Object value, Class<?> paramType) {
        if (value == null) {
            if (Boolean.TYPE.equals(paramType)) {
                return Boolean.FALSE;
            } else if (paramType.isPrimitive()) {
                if (boolean.class.isAssignableFrom(paramType)) {
                    return false;
                }
                if (long.class.isAssignableFrom(paramType)
                        || int.class.isAssignableFrom(paramType)
                        || short.class.isAssignableFrom(paramType)
                        || float.class.isAssignableFrom(paramType)
                        || double.class.isAssignableFrom(paramType)
                        || byte.class.isAssignableFrom(paramType)) {
                    return 0;
                }
                throw new IllegalStateException("Optional" + paramType.getSimpleName() + " parameter '" + name +
                        "' is present but cannot be translated into a null value due to being declared as a " +
                        "primitive type. consider declaring it sa object wrapper for the corresponding primitive type.");
            }
        }
        return value;
    }

    protected Object adaptArgumentIfNecessary(@Nullable Object arg, MethodParameter parameter) {
        if (parameter.getParameterType() == Optional.class) {
            if (arg == null || (arg instanceof Collection && ((Collection) arg).isEmpty())
                    || (arg instanceof Object[] && ((Object[]) arg).length == 0)) {
                return Optional.empty();
            } else {
                return Optional.of(arg);
            }
        }
        return arg;
    }
}
