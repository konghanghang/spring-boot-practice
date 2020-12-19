package com.ioc.annotation;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * 自定义类型扫描
 */
public class MyTypeFilter implements TypeFilter {

    /**
     *
     * @param metadataReader            读取到的当前正在扫描的类的信息
     * @param metadataReaderFactory     可以获取到其他任何类信息
     * @return
     * @throws IOException
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        // 当前类的注解信息
        final AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        // 当前类的类信息
        final ClassMetadata classMetadata = metadataReader.getClassMetadata();
        // 当前类资源（类的路径）
        final Resource resource = metadataReader.getResource();

        final String className = classMetadata.getClassName();
        System.out.println("--->" + className);
        if (className.contains("er")){
            return true;
        }
        return false;
    }
}
