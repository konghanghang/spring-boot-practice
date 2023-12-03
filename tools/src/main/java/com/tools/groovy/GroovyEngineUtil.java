package com.tools.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class GroovyEngineUtil {


    /**
     * 固定Groovy 输入的字段为input
     *
     * @param script Groovy 脚本
     * @param input  脚本输入内容
     * @return 脚本返回内容
     */
    public static String evalScript(String script, String input) {
        // 若脚本内容为空，不处理，直接返回输入内容
        if (StringUtils.isEmpty(script)) {
            return input;
        }
        Binding binding = new Binding();
        binding.setVariable("input", input);

        GroovyShell shell = new GroovyShell(binding);
        Object value;
        try {
            value = shell.evaluate(script);
        } catch (Exception e) {
            log.error("运行Groovy脚本出错，请检查配置的脚本信息，输入内容:{}, 脚本:{}", input, script, e);
            return null;
        }
        String result = value.toString();
        //log.info("转换前：{}，转换后:{}", input, result);
        return result;
    }

    /**
     * 固定Groovy 输入的字段为input，此方法script对象为线程不安全的
     *
     * @param script Groovy 脚本预编译对象
     * @param input  脚本输入内容
     * @return 脚本返回内容
     */
    public static String evalScript(Script script, String input) {
        // 若脚本内容为空，不处理，直接返回输入内容
        if (Objects.isNull(script)) {
            return input;
        }
        long start = System.currentTimeMillis();
        synchronized (script) {
            script.setProperty("input", input);
            Object value;
            try {
                value = script.run();
            } catch (Exception e) {
                log.error("运行Groovy脚本出错，请检查配置的脚本信息，输入内容:{}, 脚本:{}", input, script, e);
                return null;
            }
            String result = value.toString();
            //log.info("耗时：{}，转换前：{}，转换后:{}", System.currentTimeMillis() - start, input, result);
            return result;
        }
    }

    /**
     * 固定Groovy 输入的字段为input
     *
     * @param groovyObject Groovy 脚本预编译对象
     * @param input  脚本输入内容
     * @return 脚本返回内容
     */
    public static String evalScript(GroovyObject groovyObject, String input) {
        // 若脚本内容为空，不处理，直接返回输入内容
        if (Objects.isNull(groovyObject)) {
            return input;
        }
        long start = System.currentTimeMillis();
        Object value;
        try {
            value = groovyObject.invokeMethod("handle", input);
        } catch (Exception e) {
            log.error("运行Groovy脚本出错，请检查配置的脚本信息，输入内容:{}", input, e);
            return null;
        }
        String result = value.toString();
        //log.info("耗时：{}，转换前：{}，转换后:{}", System.currentTimeMillis() - start, input, result);
        return result;
    }

}
