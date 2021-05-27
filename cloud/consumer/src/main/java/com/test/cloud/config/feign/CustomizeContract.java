package com.test.cloud.config.feign;

import com.test.cloud.config.utils.TypeUtils;
import feign.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URI;
import java.util.*;

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

    @Override
    protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
        if (clz.getInterfaces().length == 0) {
            RequestMapping classAnnotation = AnnotatedElementUtils.findMergedAnnotation(clz,
                    RequestMapping.class);
            if (classAnnotation != null) {
                // Prepend path from class annotation if specified
                if (classAnnotation.value().length > 0) {
                    String pathValue = Util.emptyToNull(classAnnotation.value()[0]);
                    pathValue = resolve(pathValue);
                    if (!pathValue.startsWith("/")) {
                        pathValue = "/" + pathValue;
                    }
                    data.template().uri(pathValue);
                }
            }
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
        String configKey = Feign.configKey(targetType, method);
        processedMethods.put(configKey, method);
        MethodMetadata methodMetadata = createMethodMetadata();
        Type returnType = TypeUtils.resolve(targetType, targetType, method.getGenericReturnType());
        methodMetadata.returnType(returnType);
        methodMetadata.configKey(configKey);

        if (targetType.getInterfaces().length == 1) {
            this.processAnnotationOnClass(methodMetadata, targetType.getInterfaces()[0]);
        }
        this.processAnnotationOnClass(methodMetadata, targetType);
        for (final Annotation methodAnnotation : method.getAnnotations()) {
            processAnnotationOnMethod(methodMetadata, methodAnnotation, method);
        }
        setDefaultRequestMethod(methodMetadata, method);
        Map<Integer, Integer> indexMap = processParameterIndex(methodMetadata, method);
        exchangeIndex(methodMetadata, method, indexMap);
        String[] parameterNames = processParameterNames(method);
        invokeContent.setParametersName(configKey, parameterNames);

        RequestMapping classAnnotation = AnnotatedElementUtils.findMergedAnnotation(targetType,
                RequestMapping.class);
        if (classAnnotation != null) {
            // produces - use from class annotation only if method has not specified this
            if (!methodMetadata.template().headers().containsKey(ACCEPT)) {
                parseProduces(methodMetadata, method, classAnnotation);
            }

            // consumes -- use from class annotation only if method has not specified this
            if (!methodMetadata.template().headers().containsKey(CONTENT_TYPE)) {
                parseConsumes(methodMetadata, method, classAnnotation);
            }

            // headers -- class annotation is inherited to methods, always write these if
            // present
            parseHeaders(methodMetadata, method, classAnnotation);
        }
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

    private void setDefaultRequestMethod(MethodMetadata methodMetadata, Method method) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        assert requestMapping != null;
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            methodMetadata.template().method(Request.HttpMethod.POST);
        }
    }

    private Map<Integer, Integer> processParameterIndex(MethodMetadata methodMetadata, Method method) {
        Map<Integer, Integer> indexMap = new HashMap<>();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int count = parameterAnnotations.length;
        int paramIndex = 1;

        Class<?>[] parameterTypes = method.getParameterTypes();

        for (int i = 0; i < count; i++) {
            boolean isUriParameter = parameterTypes[i] == URI.class;
            if (Objects.nonNull(parameterTypes[i])){
                List<Annotation> annotations = new ArrayList<>(parameterAnnotations.length);
                boolean hasHttpAnnotation = false;
                for (Annotation parameterAnnotation : parameterAnnotations[i]) {
                    if (parameterAnnotation instanceof RequestHeader || parameterAnnotation instanceof PathVariable){
                        annotations.add(parameterAnnotation);
                        hasHttpAnnotation = true;
                    } else if (parameterAnnotation instanceof RequestParam){
                        hasHttpAnnotation = true;
                    }
                }
                Util.checkState(hasHttpAnnotation || isUriParameter,
                        "Method %s must have @RequestParam annotation on parameter %s",
                        method.getName(), i);
                if (!annotations.isEmpty()) {
                    indexMap.put(i, paramIndex);
                    processAnnotationsOnParameter(methodMetadata, annotations.toArray(new Annotation[0]), i);
                    paramIndex++;
                }
            }
            if (isUriParameter) {
                methodMetadata.urlIndex(paramIndex);
                paramIndex++;
            }
        }
        // todo
        methodMetadata.bodyIndex(0);
        methodMetadata.bodyType();
        return indexMap;
    }

    private void exchangeIndex(MethodMetadata methodMetadata, Method method, Map<Integer, Integer> indexMap) {
        Map<Integer, Collection<String>> index2Name = new LinkedHashMap<>();
        for (Map.Entry<Integer, Collection<String>> entry : methodMetadata.indexToName().entrySet()) {
            index2Name.put(indexMap.get(entry.getKey()), entry.getValue());
        }
        methodMetadata.indexToName().clear();
        for (Map.Entry<Integer, Collection<String>> entry : index2Name.entrySet()) {
            methodMetadata.indexToName().put(entry.getKey(), entry.getValue());
        }
        Map<Integer, Param.Expander> index2Expander = new LinkedHashMap<>();
        for (Map.Entry<Integer, Param.Expander> entry : methodMetadata.indexToExpander().entrySet()) {
            index2Expander.put(indexMap.get(entry.getKey()), entry.getValue());
        }
        methodMetadata.indexToExpander(index2Expander);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (Objects.nonNull(methodMetadata.headerMapIndex())) {
            checkMapString("HeaderMap", parameterTypes[methodMetadata.headerMapIndex()],
                    genericParameterTypes[methodMetadata.headerMapIndex()]);
        }
        methodMetadata.headerMapIndex(indexMap.get(methodMetadata.headerMapIndex()));
    }

    private void checkMapString(String name, Class<?> type, Type genericType) {
        Util.checkState(Map.class.isAssignableFrom(type),
                "%s parameter must be a Map: %s", name, type);
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
        Class<?> keyClass = (Class<?>) actualTypeArguments[0];
        Util.checkState(String.class.equals(keyClass),
                "%s key must be a String: %s", name, keyClass.getSimpleName());
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
