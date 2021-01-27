package com.test.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 基数排序测试
 * @author yslao@outlook.com
 * @since 2021/1/27
 */
class RadixSortTest {

    @Test
    void sort() {
        int[] arr = {1, 4, 84, 22, 3, 6, 88, 21};
        RadixSort radixSort = new RadixSort();
        radixSort.sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}