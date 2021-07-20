package com.test.cloud.config.feign;

import com.iminling.core.annotation.EnableResolve;
import java.lang.reflect.Method;
import java.util.Objects;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class CustomizeFeignUtils {

    private CustomizeFeignUtils(){}

    // 获取请求方法
    public static String getHttpMethodName(Method method) {
        String httpMethodName = "GET";
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        assert requestMapping != null;
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length > 0) {
            httpMethodName = requestMethods[0].name().toUpperCase();
        }
        return httpMethodName;
    }

    public static EnableResolve findEnableResole(Class<?> targetType, Method method) {
        EnableResolve enableResolve = AnnotatedElementUtils.findMergedAnnotation(method, EnableResolve.class);
        if (Objects.isNull(enableResolve)) {
            enableResolve = AnnotatedElementUtils.findMergedAnnotation(targetType, EnableResolve.class);
        }
        return enableResolve;
    }

}
