/*
package com.test.config.swagger;

import com.google.common.collect.Lists;
import com.iminling.core.annotation.EnableResolve;
import com.iminling.core.constant.ResolveStrategy;
import java.util.List;
import java.util.Optional;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

*/
/**
 * @author yslao@outlook.com
 * @since 2021/12/7
 *//*

//@Component
public class CustParameterBuilderPlugin implements ParameterBuilderPlugin {

    private List<String> methods = Lists.newArrayList("post", "put", "patch");

    @Override
    public void apply(ParameterContext context) {
        OperationContext operationContext = context.getOperationContext();
        Optional<EnableResolve> enableResolveOptional = operationContext.findAnnotation(EnableResolve.class);
        if (!enableResolveOptional.isPresent()) {
            enableResolveOptional = operationContext.findControllerAnnotation(EnableResolve.class);
        }
        if (enableResolveOptional.isPresent()) {
            EnableResolve enableResolve = enableResolveOptional.get();
            ResolveStrategy strategy = enableResolve.value();
            if (strategy.equals(ResolveStrategy.ARGUMENTS) || strategy.equals(ResolveStrategy.ALL)) {
                // 设置参数类型为body
                String httpMethodName = operationContext.httpMethod().name().toLowerCase();
                if (methods.contains(httpMethodName)) {
                    context.parameterBuilder().parameterType("body");
                }
            }
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
*/
