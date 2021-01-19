package com.test.sort;


import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author yslao@outlook.com
 * @since 2021/1/19
 */
class BubbleSortTest {

    @Test
    void sort() {
        int[] arr = {3, 4, 5, 10, 9};
        System.out.println("排序前:");
        System.out.println(Arrays.toString(arr));
        BubbleSort sort = new BubbleSort();
        sort.sort(arr);
        System.out.println("排序后:");
        System.out.println(Arrays.toString(arr));
    }
}