package com.tools.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yslao@outlook.com
 * @since 2021/3/30
 */
public class CustomizeClassLoader extends URLClassLoader {

    private final Logger logger = LoggerFactory.getLogger(CustomizeClassLoader.class);

    public CustomizeClassLoader(URL[] urls) {
        super(urls);
    }

    public CustomizeClassLoader(String path) throws IOException {
        super(getJarUrls(path));
    }

    private static URL[] getJarUrls(String classpath) throws IOException {
        List<URL> urls = Files.list(Paths.get(classpath)).filter(path -> path.toString().endsWith(".jar"))
                .map(Path::toUri).flatMap(uri -> {
            List<URL> list = new ArrayList<>();
            try {
                URL url = uri.toURL();
                list.add(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return list.stream();
        }).collect(Collectors.toList());
        URL[] result = new URL[urls.size()];
        urls.toArray(result);
        return result;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        logger.info("find class, name:{}, class;{}", name, loadedClass);
        if (loadedClass == null) {
            try {
                loadedClass = super.findClass(name);
            } catch (ClassNotFoundException e) {

            }
        }
        return super.findClass(name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }
}
