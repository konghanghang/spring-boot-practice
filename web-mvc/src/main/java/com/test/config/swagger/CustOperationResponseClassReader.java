/*
package com.test.config.swagger;

import static java.util.stream.Collectors.toList;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.iminling.core.annotation.EnableResolve;
import com.iminling.core.config.value.ResultModel;
import com.iminling.core.constant.ResolveStrategy;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.schema.Types;
import springfox.documentation.schema.plugins.SchemaPluginsManager;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.schema.ViewProviderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.ResponseMessagesReader;

*/
/**
 * @author yslao@outlook.com
 * @since 2021/12/7
 *//*

//@Component
public class CustOperationResponseClassReader implements OperationBuilderPlugin {

    private static Logger log = LoggerFactory.getLogger(CustOperationResponseClassReader.class);

    private List<String> methods = Lists.newArrayList("post", "put", "patch");

    private final EnumTypeDeterminer enumTypeDeterminer;
    private final TypeNameExtractor nameExtractor;
    private final SchemaPluginsManager pluginsManager;
    private final TypeResolver resolver;
    private final DocumentationPluginsManager documentationPluginsManager;

    @Autowired
    public CustOperationResponseClassReader(
        SchemaPluginsManager pluginsManager,
        EnumTypeDeterminer enumTypeDeterminer,
        TypeNameExtractor nameExtractor,
        DocumentationPluginsManager documentationPluginsManager,
        TypeResolver resolver) {
        this.enumTypeDeterminer = enumTypeDeterminer;
        this.nameExtractor = nameExtractor;
        this.pluginsManager = pluginsManager;
        this.resolver = resolver;
        this.documentationPluginsManager = documentationPluginsManager;
    }

    @Override
    public void apply(OperationContext context) {
        Optional<EnableResolve> enableResolveOptional = context.findAnnotation(EnableResolve.class);
        if (!enableResolveOptional.isPresent()) {
            enableResolveOptional = context.findControllerAnnotation(EnableResolve.class);
        }
        if (enableResolveOptional.isPresent()) {
            EnableResolve enableResolve = enableResolveOptional.get();
            ResolveStrategy strategy = enableResolve.value();
            // 处理返回值
            if (strategy.equals(ResolveStrategy.RETURN_VALUE) || strategy.equals(ResolveStrategy.ALL)) {
                ResolvedType returnType = resolver.resolve(ResultModel.class, context.getReturnType());
                returnType = context.alternateFor(returnType);

                ViewProviderPlugin viewProvider =
                    pluginsManager.viewProvider(context.getDocumentationContext().getDocumentationType());

                ModelContext modelContext = context.operationModelsBuilder().addReturn(
                    returnType,
                    viewProvider.viewFor(returnType, context));

                Map<String, String> knownNames = new HashMap<>();
                Optional.ofNullable(context.getKnownModels().get(modelContext.getParameterId()))
                    .orElse(new HashSet<>())
                    .forEach(model -> knownNames.put(model.getId(), model.getName()));

                String responseTypeName = nameExtractor.typeName(modelContext);
                log.debug("Setting spring response class to: {}", responseTypeName);

                context.operationBuilder().responseModel(
                    ResolvedTypes.modelRefFactory(modelContext, enumTypeDeterminer, nameExtractor, knownNames).apply(returnType));

                // 处理ResponseMessages 200状态的情况
                List<ResponseMessage> responseMessages = context.getGlobalResponseMessages(context.httpMethod().toString());
                context.operationBuilder().responseMessages(new HashSet<>(responseMessages));
                applyReturnTypeOverride(context, returnType, modelContext);
            }
            // 处理参数
            if (strategy.equals(ResolveStrategy.ARGUMENTS) || strategy.equals(ResolveStrategy.ALL)) {
                String httpMethodName = context.httpMethod().name().toLowerCase();
                if (methods.contains(httpMethodName)) {
                    OperationBuilder operationBuilder = context.operationBuilder();
                    try {
                        Field parameters = operationBuilder.getClass().getDeclaredField("parameters");
                        parameters.setAccessible(true);
                        parameters.set(operationBuilder, new ArrayList<>());
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        log.error("CustOperationResponseClassReader error", e);
                    }
                    operationBuilder.parameters(readParameters(context));
                }
            }
        }

    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    */
/**
     * 参考springfox.documentation.spring.web.readers.operation.ResponseMessagesReader
     * @param context
     * @param returnType
     * @param modelContext
     *//*

    private void applyReturnTypeOverride(OperationContext context, ResolvedType returnType, ModelContext modelContext) {
        int httpStatusCode = ResponseMessagesReader.httpStatusCode(context);
        String message = ResponseMessagesReader.message(context);
        ModelReference modelRef = null;

        if (!Types.isVoid(returnType)) {
            Map<String, String> knownNames = new HashMap<>();
            Optional.ofNullable(context.getKnownModels().get(modelContext.getParameterId()))
                .orElse(new HashSet<>())
                .forEach(model -> knownNames.put(
                    model.getId(),
                    model.getName()));

            modelRef = ResolvedTypes.modelRefFactory(
                modelContext,
                enumTypeDeterminer,
                nameExtractor,
                knownNames).apply(returnType);
        }
        ResponseMessage built = new ResponseMessageBuilder().code(httpStatusCode).message(message).responseModel(modelRef)
            .build();
        context.operationBuilder().responseMessages(Collections.singleton(built));
    }

    */
/**
     * springfox.documentation.spring.web.readers.operation.OperationParameterReader
     * @param context
     * @return
     *//*

    private List<Parameter> readParameters(final OperationContext context) {
        List<Parameter> parameters = new ArrayList<>();
        try {
            String className = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, context.getGroupName()) + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, context.getName());
            Class<?> aClass = Class.forName("com.iminling.javassist.model." + className);
            ResolvedType resolvedType = resolver.resolve(aClass);
            ResolvedMethodParameter bodyParameter = new ResolvedMethodParameter(0, "json", new ArrayList<>(), resolvedType);
            ParameterContext parameterContext = new ParameterContext(bodyParameter,
                new ParameterBuilder(),
                context.getDocumentationContext(),
                context.getGenericsNamingStrategy(),
                context);
            parameters.add(documentationPluginsManager.parameter(parameterContext));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parameters.stream().filter(((Predicate<Parameter>) Parameter::isHidden).negate()).collect(toList());
    }

}
*/
