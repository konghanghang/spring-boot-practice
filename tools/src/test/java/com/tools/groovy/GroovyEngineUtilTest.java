package com.tools.groovy;

import static org.junit.jupiter.api.Assertions.*;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.junit.jupiter.api.Test;

class GroovyEngineUtilTest {

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Test
    void evalScript_scriptStr() {

        String script = "return input";
        long l = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            System.out.println(GroovyEngineUtil.evalScript(script, i + ""));
        }
        System.out.println("执行耗时：" + (System.currentTimeMillis() - l));
    }

    @Test
    void evalScript_scriptObj() throws InterruptedException {

        String script = "return input";

        GroovyShell shell = new GroovyShell();
        Script parse = shell.parse(script);
        long l = System.currentTimeMillis();

        int count = 1000000;
        CountDownLatch latch = new CountDownLatch(count);
        // Map<String, LongAdder> map = new ConcurrentHashMap<>();
        for (int i = 0; i < count; i++) {
            int a = i;
            executorService.execute(() -> {
                synchronized (script) {
                    String value = GroovyEngineUtil.evalScript(parse, a + "");;
                    // System.out.println(value);
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println("执行耗时：" + (System.currentTimeMillis() - l));
    }

    @Test
    void evalScript_groovyObj() throws InterruptedException, InstantiationException, IllegalAccessException {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        String groovyClass = """
            package com.tools.groovy
            class HelloWorld {
                String handle(String name) {
                    return name
                }
            }
            """;
        Class helloClass = groovyClassLoader.parseClass(groovyClass);
        GroovyObject object = (GroovyObject) helloClass.newInstance();
        long l = System.currentTimeMillis();
        Integer count = 1000000;
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            int a = i;
            executorService.execute(() -> {
                String value = GroovyEngineUtil.evalScript(object, a + "");
                //System.out.println(value);
                latch.countDown();
            });
        }
        latch.await();
        System.out.println("执行耗时：" + (System.currentTimeMillis() - l));
    }

    @Test
    void evalScript_ScriptEngine() throws InterruptedException, ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");// 每次生成一个engine实例
        String groovyClass = """
            def handleOne(String name) {
                     return "姓名：" + name
                 }
            """;
        engine.eval(groovyClass);
        long l = System.currentTimeMillis();
        Integer count = 20;
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            int a = i;
            executorService.execute(() -> {
                try {
                    String value = (String) ((Invocable) engine).invokeFunction("handleOne", a + "");
                    // System.out.println(value);
                } catch (ScriptException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

                latch.countDown();
            });
        }
        latch.await();
        System.out.println("执行耗时：" + (System.currentTimeMillis() - l));
    }

}