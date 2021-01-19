package com.test.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/1/19
 */
class SelectSortTest {

    @Test
    void sort() {
        int[] arr = {12, 1, 7, 5, 0};
        System.out.println("排序前:");
        System.out.println(Arrays.toString(arr));
        SelectSort sort = new SelectSort();
        sort.sort(arr);
        System.out.println("排序后:");
        System.out.println(Arrays.toString(arr));
    }
}