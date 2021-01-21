package com.test.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/1/21
 */
class ShellSortTest {

    @Test
    void sort() {
        int[] arr = {8, 4, 9, 10, 3, 6, 1};
        System.out.println("排序前:");
        System.out.println(Arrays.toString(arr));
        ShellSort sort = new ShellSort();
        sort.sort(arr);
        System.out.println("排序后:");
        System.out.println(Arrays.toString(arr));
    }
}