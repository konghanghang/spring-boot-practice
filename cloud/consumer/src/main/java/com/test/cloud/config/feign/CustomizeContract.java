package com.test.cloud.config.feign;

import static feign.Util.checkState;
import static feign.Util.emptyToNull;
import static java.util.Optional.ofNullable;

import com.iminling.core.annotation.EnableResolve;
import com.test.cloud.config.utils.TypeUtils;
import feign.Feign;
import feign.MethodMetadata;
import feign.Param.ToStringExpander;
import feign.Util;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 自定义契约
 * @author yslao@outlook.com
 * @since 2021/3/26
 */
public class CustomizeContract extends SpringMvcContract {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizeContract.class);

    private static final String ACCEPT = "Accept";

    private static final String CONTENT_TYPE = "Content-Type";

    private ResourceLoader resourceLoader;
    private Map<String, Method> processedMethods;
    private InvokeContent invokeContent;;

    public CustomizeContract(InvokeContent invokeContent) {
        try {
            // 通过反射拿到SpringMvcContract中的resourceLoader
            Field resourceLoaderField = SpringMvcContract.class.getDeclaredField("resourceLoader");
            resourceLoaderField.setAccessible(true);
            resourceLoader = (ResourceLoader) resourceLoaderField.get(this);

            // 通过反射拿到SpringMvcContract中的processedMethods
            Field processedMethodsField = SpringMvcContract.class.getDeclaredField("processedMethods");
            processedMethodsField.setAccessible(true);
            processedMethods = (Map<String, Method>) processedMethodsField.get(this);
            this.invokeContent = invokeContent;
            LOGGER.info("customizeContract init end");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("init CustomizeContract error", e);
        }
    }

    /**
     * 参考org.springframework.cloud.openfeign.support.SpringMvcContract的实现
     * @param targetType
     * @param method
     * @return
     */
    @Override
    public MethodMetadata parseAndValidateMetadata(Class<?> targetType, Method method) {
        String httpMethodName = CustomizeFeignUtils.getHttpMethodName(method);
        EnableResolve enableResolve = CustomizeFeignUtils.findEnableResole(targetType, method);
        String configKey = Feign.configKey(targetType, method);
        processedMethods.put(configKey, method);
        // 如果是get请求直接调用springMvcContract进行处理
        if ("GET".equals(httpMethodName) || Objects.isNull(enableResolve)) {
            return super.parseAndValidateMetadata(targetType, method);
        }
        MethodMetadata methodMetadata = createMethodMetadata();
        Type returnType = TypeUtils.resolve(targetType, targetType, method.getGenericReturnType());
        methodMetadata.returnType(returnType);
        methodMetadata.configKey(configKey);
        if (targetType.getInterfaces().length == 1) {
            super.processAnnotationOnClass(methodMetadata, targetType.getInterfaces()[0]);
        }
        super.processAnnotationOnClass(methodMetadata, targetType);
        for (final Annotation methodAnnotation : method.getAnnotations()) {
            super.processAnnotationOnMethod(methodMetadata, methodAnnotation, method);
        }
        processParameterIndex(methodMetadata, method);
        String[] parameterNames = processParameterNames(method);
        invokeContent.setParametersName(configKey, parameterNames);

        // ==========================下边的从SpringMvcContract搬运==========================
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(targetType, RequestMapping.class);
        if (requestMapping != null) {
            // produces - use from class annotation only if method has not specified this
            if (!methodMetadata.template().headers().containsKey(ACCEPT)) {
                parseProduces(methodMetadata, method, requestMapping);
            }

            // consumes -- use from class annotation only if method has not specified this
            if (!methodMetadata.template().headers().containsKey(CONTENT_TYPE)) {
                parseConsumes(methodMetadata, method, requestMapping);
            }

            // headers -- class annotation is inherited to methods, always write these if
            // present
            parseHeaders(methodMetadata, method, requestMapping);
        }
        // ==========================从SpringMvcContract搬运结束==========================
        return methodMetadata;
    }

    /**
     * 创建MethodMetadata，因为MethodMetadata的构造方法不是public
     * 所以需要利用反射来创建对象
     * @return
     */
    private MethodMetadata createMethodMetadata() {
        try {
            Constructor<MethodMetadata> declaredConstructor = MethodMetadata.class.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            MethodMetadata methodMetadata = declaredConstructor.newInstance();
            return methodMetadata;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 搬运自SpringMvcContract
     * @param value
     * @return
     */
    private String resolve(String value) {
        if (StringUtils.hasText(value)
                && resourceLoader instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) resourceLoader).getEnvironment()
                    .resolvePlaceholders(value);
        }
        return value;
    }

    private Map<Integer, Integer> processParameterIndex(MethodMetadata methodMetadata, Method method) {
        Map<Integer, Integer> indexMap = new HashMap<>();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int count = parameterAnnotations.length;
        int paramIndex = 1;

        Class<?>[] parameterTypes = method.getParameterTypes();

        for (int i = 0; i < count; i++) {
            boolean isUriParameter = parameterTypes[i] == URI.class;
            if (isUriParameter) {
                methodMetadata.urlIndex(paramIndex);
                paramIndex++;
                continue;
            }
            for (Annotation parameterAnnotation : parameterAnnotations[i]) {
                Class<?> parameterType = parameterTypes[i];
                // RequestHeader暂时先写在这里，实际应该用不上
                if (parameterAnnotation instanceof RequestHeader) {
                    if (Map.class.isAssignableFrom(parameterType)) {
                        checkState(methodMetadata.headerMapIndex() == null,
                            "Header map can only be present once.");
                        methodMetadata.headerMapIndex(paramIndex);
                    } else {
                        String name = ((RequestHeader) parameterAnnotation).value();
                        Collection<String> params = ofNullable(methodMetadata.template().headers().get(name))
                            .map(ArrayList::new)
                            .orElse(new ArrayList<>());
                        params.add(String.format("{%s}", name));
                        methodMetadata.template().header(name, params);
                    }
                    methodMetadata.ignoreParamater(paramIndex);
                } else if (parameterAnnotation instanceof PathVariable) {
                    methodMetadata.ignoreParamater(paramIndex);
                    String name = ((PathVariable) parameterAnnotation).value();
                    super.nameParam(methodMetadata, name, paramIndex);
                    methodMetadata.indexToExpander().put(paramIndex, new ToStringExpander());
                } else if (parameterAnnotation instanceof RequestParam) {
                    Class<RequestParam> ANNOTATION = RequestParam.class;
                    RequestParam requestParam = ANNOTATION.cast(parameterAnnotation);
                    String name = requestParam.value();
                    checkState(emptyToNull(name) != null,
                        "RequestParam.value() was empty on parameter %s", i);
                    methodMetadata.ignoreParamater(paramIndex);
                } else {
                    Util.checkState(true,
                        "Method %s must have @RequestParam annotation on parameter %s",
                        method.getName(), i);
                }
                paramIndex++;
            }
        }
        methodMetadata.bodyIndex(0);
        methodMetadata.bodyType(TypeUtils.resolve(Map.class, Map.class, Map.class));
        return indexMap;
    }

    private String[] processParameterNames(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int parameterCount = method.getParameterCount();
        String[] parameterNames = new String[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestParam) {
                    RequestParam requestParam = (RequestParam) annotation;
                    String name = requestParam.value();
                    Util.checkState(Util.emptyToNull(name) != null,
                            "RequestParam.value() was empty on parameter %s", i);
                    parameterNames[i] = name;
                }
            }
        }
        return parameterNames;
    }

    protected void nameParam(MethodMetadata data, String name, int i) {
        final Collection<String> names =
            data.indexToName().containsKey(i) ? data.indexToName().get(i) : new ArrayList<String>();
        names.add(name);
        data.indexToName().put(i, names);
    }

    private void parseProduces(MethodMetadata md, Method method,
                               RequestMapping annotation) {
        String[] serverProduces = annotation.produces();
        String clientAccepts = serverProduces.length == 0 ? null
                : Util.emptyToNull(serverProduces[0]);
        if (clientAccepts != null) {
            md.template().header(ACCEPT, clientAccepts);
        }
    }

    private void parseConsumes(MethodMetadata md, Method method,
                               RequestMapping annotation) {
        String[] serverConsumes = annotation.consumes();
        String clientProduces = serverConsumes.length == 0 ? null
                : Util.emptyToNull(serverConsumes[0]);
        if (clientProduces != null) {
            md.template().header(CONTENT_TYPE, clientProduces);
        }
    }

    private void parseHeaders(MethodMetadata md, Method method,
                              RequestMapping annotation) {
        // only supports one header value per key
        if (annotation.headers() != null && annotation.headers().length > 0) {
            for (String header : annotation.headers()) {
                int index = header.indexOf('=');
                if (!header.contains("!=") && index >= 0) {
                    md.template().header(resolve(header.substring(0, index)),
                            resolve(header.substring(index + 1).trim()));
                }
            }
        }
    }
}
