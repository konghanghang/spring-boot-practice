package com.test.kmp;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * kmp算法测试
 * @author yslao@outlook.com
 * @since 2021/4/3
 */
class KMPTest {

    @Test
    void kmp() {

        String str = "BBC ABCDAB ABCDABCDABDE";
        String subStr = "ABCDABD";
        KMP kmp = new KMP();
        int[] next = kmp.getNext(subStr);
        System.out.println(Arrays.toString(next));
        System.out.println(kmp.kmpSearch(str, subStr, next));
    }
}