package com.test.type;

import java.lang.reflect.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author yslao@outlook.com
 * @since 2022/3/3
 */
public class TypeTest {

    @Test
    void parameterTest() throws NoSuchFieldException {
        Field fieldList = User.class.getDeclaredField("list");
        // 获取该属性的泛型类型
        Type typeList = fieldList.getGenericType();
        Assertions.assertTrue("java.util.List<T>".equals(typeList.getTypeName()));
        System.out.println("list域实际的Type类型："+typeList.getClass().getName());

        System.out.println("-----------------------------------------------------");
        Field fieldMap = User.class.getDeclaredField("map");
        // 获取该属性的泛型类型
        Type typeMap = fieldMap.getGenericType();
        System.out.println("map域类型名："+typeMap.getTypeName());
        System.out.println("map域实际的Type类型："+typeMap.getClass().getName());
        if (typeMap instanceof ParameterizedType){
            ParameterizedType mapParameterizedType = (ParameterizedType) typeMap;
            // 获取泛型中的实际参数
            Type[] types = mapParameterizedType.getActualTypeArguments();
            for (Type type : types) {
                System.out.println("map域泛型参数类型:" + type);
            }
            System.out.println("map域声明泛型参数的类类型：" + mapParameterizedType.getRawType());
            System.out.println("泛型的拥有者类型：" + mapParameterizedType.getOwnerType());
        }
        System.out.println("-----------------------------------------------------");
        // 获取ParameterizedTypeTest类的list属性
        Field fieldMap2 = User.class.getDeclaredField("map2");
        // 获取该属性的泛型类型
        Type typeMap2 = fieldMap2.getGenericType();
        System.out.println("map2域类型名："+typeMap2.getTypeName());
        System.out.println("map2域实际的Type类型："+typeMap2.getClass().getName());
        if (typeMap2 instanceof ParameterizedType){
            ParameterizedType mapParameterizedType2 = (ParameterizedType) typeMap2;
            // 获取泛型中的实际参数
            Type[] types = mapParameterizedType2.getActualTypeArguments();
            for (Type type : types) {
                System.out.println("map域泛型参数类型:" + type);
            }
            System.out.println("map2域声明泛型参数的类类型："+mapParameterizedType2.getRawType());
            System.out.println("泛型的拥有者类型："+mapParameterizedType2.getOwnerType());
        }
    }

    @Test
    void typeVariableTest() throws NoSuchFieldException {
        Field aField = User.class.getDeclaredField("t");
        Type aFieldType = aField.getGenericType();
        Class aFieldClass = aField.getType();
        System.out.println("type名："+aFieldType.getTypeName());
        System.out.println("type的class名："+aFieldType.getClass().getCanonicalName());
        System.out.println("Class名: "+aFieldClass.getCanonicalName());

        if (aFieldType instanceof TypeVariable){
            TypeVariable typeVariable = (TypeVariable) aFieldType;
            String name = typeVariable.getName();
            Type[] bounds = typeVariable.getBounds();
            System.out.println("type'name: "+name);
            for (int i=0;i<bounds.length;i++){
                System.out.println("域 type'bounds["+i+"] Type'name: "+bounds[i].getTypeName());
            }
            GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
            System.out.println("声明a 域变量的实体："+genericDeclaration);
        }
    }

    @Test
    void genericArrayTypeTest() throws NoSuchFieldException {
        Field fieldListArray = User.class.getDeclaredField("lists");
        Type typeListArray = fieldListArray.getGenericType();
        System.out.println("lists域Type类型："+typeListArray.getClass().getName());
        GenericArrayType genericArrayType1 = (GenericArrayType) typeListArray;
        Assertions.assertTrue("java.util.List<java.lang.String>".equals(genericArrayType1.getGenericComponentType().getTypeName()));
        System.out.println("......................................................\n");

        Field fieldT = User.class.getDeclaredField("ts");
        Type typeT = fieldT.getGenericType();
        System.out.println("t域Type类型："+typeT.getClass().getName());
        GenericArrayType genericArrayType2 = (GenericArrayType) typeT;
        Assertions.assertTrue("T".equals(genericArrayType2.getGenericComponentType().getTypeName()));
    }

    @Test
    void wildcardTypeTest() throws NoSuchFieldException {
        Field aa = User.class.getDeclaredField("aa");
        Type genericType = aa.getGenericType();
        System.out.println("aa域Type类型："+genericType.getTypeName());
        if (genericType instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (Type type : actualTypeArguments) {
                System.out.println("aa域泛型参数类型:" + type.getTypeName());
                if (type instanceof WildcardType) {
                    WildcardType wildcardType = (WildcardType) type;
                    Type[] type0UpperBounds = wildcardType.getUpperBounds();
                    for (Type type0 : type0UpperBounds) {
                        System.out.println("type0 upper:" + type0.getTypeName());
                    }
                    Type[] type0LowerBounds = wildcardType.getLowerBounds();
                    for (Type type0 : type0LowerBounds) {
                        System.out.println("type0 lower:" + type0.getTypeName());
                    }
                }
            }
        }
    }

}
