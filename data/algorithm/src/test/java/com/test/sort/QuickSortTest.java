package com.test.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class QuickSortTest {

    @Test
    void sort() {
        int[] arr = {10, 4, 6, 5, 5, 5, 5, 6, 9, 10};
        System.out.println("排序前:");
        System.out.println(Arrays.toString(arr));
        QuickSort quickSort = new QuickSort();
        // quickSort.sort(arr, 0, arr.length - 1);
        quickSort.sort2(arr, 0, arr.length - 1);
        System.out.println("排序后:");
        System.out.println(Arrays.toString(arr));
    }
}