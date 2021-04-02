package com.tools.classloader;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/3/30
 */
class CustomizeClassLoaderTest {

    @Test
    void testLoader() throws IOException {
        CustomizeClassLoader customizeClassLoader = new CustomizeClassLoader("D:\\project\\idea\\iminling-web\\target\\iminling-web\\lib");
    }

}