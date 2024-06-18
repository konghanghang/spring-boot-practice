/*
package com.test.config.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.CaseFormat;
import com.iminling.core.annotation.EnableResolve;
import com.iminling.core.config.value.ResultModel;
import com.iminling.core.constant.ResolveStrategy;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javassist.*;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.schema.plugins.SchemaPluginsManager;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ViewProviderPlugin;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

*/
/**
 * @author yslao@outlook.com
 * @since 2021/12/6
 *//*

//@Component
public class CustOperationModelsProvider implements OperationModelsProviderPlugin {

    private final SchemaPluginsManager pluginsManager;
    private final TypeResolver resolver;

    @Autowired
    public CustOperationModelsProvider(SchemaPluginsManager pluginsManager, TypeResolver resolver) {
        this.pluginsManager = pluginsManager;
        this.resolver = resolver;
    }

    @Override
    public void apply(RequestMappingContext context) {
        // 获取方法和类上的所有EnableResolve注解
        List<EnableResolve> annotations = context.findAnnotations(EnableResolve.class);
        EnableResolve enableResolve = null;
        if (annotations.size() > 0) {
            // 获取方法上的EnableResolve注解
            Optional<EnableResolve> optionalEnableResolve = context.findAnnotation(EnableResolve.class);
            if (optionalEnableResolve.isPresent()) {
                enableResolve = optionalEnableResolve.get();
            } else {
                enableResolve = annotations.get(0);
            }
        }
        if (enableResolve != null) {
            ResolveStrategy strategy = enableResolve.value();
            if (strategy.equals(ResolveStrategy.ARGUMENTS) || strategy.equals(ResolveStrategy.ALL)) {
                List<ResolvedMethodParameter> methodParameters = context.getParameters();
                ClassPool classPool = ClassPool.getDefault();
                //创建类名
                String className = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, context.getGroupName()) + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, context.getName());
                CtClass ctClass = classPool.makeClass("com.iminling.javassist.model." + className);
                for (ResolvedMethodParameter methodParameter : methodParameters) {
                    Optional<String> fieldNameOptional = methodParameter.defaultName();
                    String typeName = methodParameter.getParameterType().getTypeName();
                    try {
                        String fieldName = "unknown";
                        if (fieldNameOptional.isPresent()) {
                            fieldName = fieldNameOptional.get();
                        }
                        CtField ctField = new CtField(classPool.get(typeName), fieldName, ctClass);
                        String upperName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
                        ctClass.addMethod(CtNewMethod.setter("set" + upperName, ctField));
                        ctClass.addMethod(CtNewMethod.getter("get" + upperName, ctField));
                        // 添加注解
                        addAnnotation(context, ctClass, ctField);
                        ctField.getFieldInfo().setAccessFlags(AccessFlag.PRIVATE);
                        ctClass.addField(ctField);
                    } catch (CannotCompileException | NotFoundException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    ResolvedType resolve = resolver.resolve(ctClass.toClass());
                    context.operationModelsBuilder().addReturn(
                        resolve,
                        viewForReturn(context, resolve));
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        }
        ResolvedType modelType = resolver.resolve(ResultModel.class, context.getReturnType());
        modelType = context.alternateFor(modelType);
        context.operationModelsBuilder().addReturn(
            modelType,
            viewForReturn(context, modelType));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private Optional<ResolvedType> viewForReturn(
        RequestMappingContext context,
        ResolvedType regularModel) {
        ViewProviderPlugin viewProvider =
            pluginsManager.viewProvider(context.getDocumentationContext().getDocumentationType());
        return viewProvider.viewFor(
            regularModel,
            context);
    }

    private void addAnnotation(RequestMappingContext context, CtClass ctClass, CtField ctField) {
        Optional<ApiImplicitParam> implicitParamOptional = context.findAnnotation(ApiImplicitParam.class);
        if (implicitParamOptional.isPresent()) {
            // 就一个参数
            ApiImplicitParam apiImplicitParam = implicitParamOptional.get();
            String value = apiImplicitParam.value();
            ConstPool constPool = ctClass.getClassFile().getConstPool();
            AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation ann = new Annotation(ApiModelProperty.class.getName(), constPool);
            ann.addMemberValue("value", new StringMemberValue(value, constPool));
            attr.addAnnotation(ann);
            ctField.getFieldInfo().addAttribute(attr);
        } else {
            context.findAnnotation(ApiImplicitParams.class).ifPresent(implicitParams -> {
                Arrays.stream(implicitParams.value())
                    .filter(apiImplicitParam -> ctField.getName().equals(apiImplicitParam.name()))
                    .findFirst()
                    .ifPresent(param -> {
                        ConstPool constPool = ctClass.getClassFile().getConstPool();
                        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
                        Annotation ann = new Annotation(ApiModelProperty.class.getName(), constPool);
                        ann.addMemberValue("value", new StringMemberValue(param.value(), constPool));
                        attr.addAnnotation(ann);
                        ctField.getFieldInfo().addAttribute(attr);
                });
            });
        }
    }
}
*/
