package com.tools.regex;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author yslao@outlook.com
 * @since 2022/1/28
 */
@Slf4j
public class RegexTest {

    @Test
    void compileTest() {
        Pattern compile = Pattern.compile("");
        compile = Pattern.compile("", Pattern.CASE_INSENSITIVE);
    }

    @Test
    void splitTest() {
        String str = "123Java456Java789Java";
        Pattern pattern = Pattern.compile("Java");
        String[] split = pattern.split(str);
        log.info("split: {}", Arrays.toString(split));
        split = pattern.split(str, 2);
        log.info("split: {}", Arrays.toString(split));
        split = pattern.split(str, 5);
        log.info("split: {}", Arrays.toString(split));
        split = pattern.split(str, -5);
        log.info("split: {}", Arrays.toString(split));
    }

    @Test
    void matchesTest() {
        Pattern pattern = Pattern.compile("Java");
        boolean matches = pattern.matcher("Java").matches();
        log.info("matches: {}", matches);
        matches = pattern.matcher("Java1234").matches();
        log.info("matches: {}", matches);
    }

    @Test
    void lookAtTest() {
        Pattern pattern = Pattern.compile("Java");
        Matcher matcher = pattern.matcher("Java1234");
        log.info("lookingAt: {}", matcher.lookingAt());
        matcher = pattern.matcher("1234Java");
        log.info("lookingAt: {}", matcher.lookingAt());
    }

    @Test
    void findTest() {
        String str = "123Java456Java789Java";
        Pattern pattern = Pattern.compile("Java");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            log.info("find: {}, start:{}, end:{}", matcher.group(), matcher.start(), matcher.end());
        }
    }

    @Test
    void findStartTest() {
        String str = "123Java456Java789Java";
        Pattern pattern = Pattern.compile("Java");
        Matcher matcher = pattern.matcher(str);
        boolean b = matcher.find(1);
        log.info("find1: {}, start:{}, end:{}", matcher.group(), matcher.start(), matcher.end());
        b = matcher.find(5);
        log.info("find2: {}, start:{}, end:{}", matcher.group(), matcher.start(), matcher.end());
        b = matcher.find(14);
        log.info("find3: {}, start:{}, end:{}", matcher.group(), matcher.start(), matcher.end());
    }

    @Test
    void regionTest() {
        String str = "123Java456Java789Java";
        Pattern pattern = Pattern.compile("Java");
        Matcher matcher = pattern.matcher(str);
        matcher.region(1, 20);
        log.info("region, start:{}, end:{}", matcher.regionStart(), matcher.regionEnd());
        while (matcher.find()) {
            log.info("find: {}, start:{}, end:{}", matcher.group(), matcher.start(), matcher.end());
        }
    }

    @Test
    void resetTest() {
        String str = "123Java456Java789Java";
        Pattern pattern = Pattern.compile("Java");
        Matcher matcher = pattern.matcher(str);
        matcher.find();
        log.info("find1: {}, start:{}, end:{}", matcher.group(), matcher.start(), matcher.end());
        matcher.find();
        log.info("find2: {}, start:{}, end:{}", matcher.group(), matcher.start(), matcher.end());
        // 进行重置，又从字符串开头进行查找
        matcher.reset();
        matcher.find();
        log.info("find3: {}, start:{}, end:{}", matcher.group(), matcher.start(), matcher.end());
        // 重置为其他无法匹配到的字符串
        matcher.reset("1234");
        boolean b = matcher.find();
        if (b) {
            log.info("find4: {}, start:{}, end:{}", matcher.group(), matcher.start(), matcher.end());
        }
        // 重置为其他可以匹配到的字符串
        matcher.reset("1234Java");
        matcher.find();
        log.info("find5: {}, start:{}, end:{}", matcher.group(), matcher.start(), matcher.end());
    }

    @Test
    void groupTest() {
        String str = "13101000200aaa13103000400";
        Pattern pattern = Pattern.compile("(\\d{3})\\d{4}(\\d{4})");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                log.info("group{}: {}", i, matcher.group(i));
            }
            log.info("find: {}, start:{}, end:{}", matcher.group(2), matcher.start(), matcher.end());
        }
    }

    @Test
    void replaceTest() {
        String str = "13101000200aaa13103000400";
        Pattern pattern = Pattern.compile("(\\d{3})\\d{4}(\\d{4})");
        Matcher matcher = pattern.matcher(str);
        String s = matcher.replaceFirst("$1****$2");
        log.info("replace: {}", s);
        s = matcher.replaceAll("$1****$2");
        log.info("replace: {}", s);
    }

    @Test
    void appendReplacementTest() {
        String str = "123Java456Java789Java123";
        Pattern pattern = Pattern.compile("Java");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            log.info("append");
            matcher.appendReplacement(sb, "JAVA");
        }
        log.info("appendReplacement: {}", sb);
    }

    @Test
    void appendTailTest() {
        String str = "123Java456Java789Java123";
        Pattern pattern = Pattern.compile("Java");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        if (matcher.find()) {
            log.info("append");
            matcher.appendReplacement(sb, "JAVA");
        }
        matcher.appendTail(sb);
        log.info("appendTail: {}", sb);
    }
}
