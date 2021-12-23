package com.test.config.swagger;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.iminling.core.annotation.swagger.SwaggerDisplayEnum;
import io.swagger.annotations.ApiModelProperty;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.schema.Annotations;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

/**
 * @author yslao@outlook.com
 * @since 2021/12/8
 */
//@Component
public class CustModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

    @Override
    public void apply(ModelPropertyContext context) {
        Optional<BeanPropertyDefinition> beanPropertyDefinitionOptional = context.getBeanPropertyDefinition();
        if (!beanPropertyDefinitionOptional.isPresent()) {
            return;
        }
        BeanPropertyDefinition beanPropertyDefinition = beanPropertyDefinitionOptional.get();
        final Class<?> fieldType = beanPropertyDefinition.getField().getRawType();
        if (!Enum.class.isAssignableFrom(fieldType)) {
            return;
        }
        SwaggerDisplayEnum swaggerDisplayEnum = AnnotationUtils.findAnnotation(fieldType, SwaggerDisplayEnum.class);
        if (swaggerDisplayEnum == null) {
            return;
        }
        String code = swaggerDisplayEnum.code();
        String desc = swaggerDisplayEnum.desc();

        Object[] enumConstants = fieldType.getEnumConstants();
        List<String> allowableValues = new ArrayList<>();
        List<String> displayValues = Arrays.stream(enumConstants)
                .filter(Objects::nonNull)
                .map(item -> {
                    Class<?> currentClass = item.getClass();

                    Field indexField = ReflectionUtils.findField(currentClass, code);
                    ReflectionUtils.makeAccessible(indexField);
                    Object value = ReflectionUtils.getField(indexField, item);

                    Field descField = ReflectionUtils.findField(currentClass, desc);
                    ReflectionUtils.makeAccessible(descField);
                    Object obj = ReflectionUtils.getField(descField, item);
                    if (indexField.getAnnotation(JsonValue.class) != null) {
                        allowableValues.add(String.valueOf(value));
                    } else if (descField.getAnnotation(JsonValue.class) != null){
                        allowableValues.add(String.valueOf(obj));
                    } else {
                        allowableValues.add(String.valueOf(value));
                    }
                    return value + ":" + obj;
                }).collect(Collectors.toList());

        ModelPropertyBuilder builder = context.getBuilder();
        String displayName = "enum";
        Optional<ApiModelProperty> apiModelPropertyOptional = Annotations.findPropertyAnnotation(beanPropertyDefinition,
            ApiModelProperty.class);
        if (apiModelPropertyOptional.isPresent()) {
            ApiModelProperty apiModelProperty = apiModelPropertyOptional.get();
            displayName = apiModelProperty.value();
        }
        String joinText = displayName + " (" + String.join("; ", displayValues) + ")";
        builder.description(joinText).type(context.getResolver().resolve(fieldType));
        AllowableListValues values = new AllowableListValues(allowableValues, "LIST");
        builder.allowableValues(values);
        builder.type(context.getResolver().resolve(fieldType));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
