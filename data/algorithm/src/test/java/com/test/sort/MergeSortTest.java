package com.test.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/1/26
 */
class MergeSortTest {

    @Test
    void sort() {
        int[] arr = {2, 6, 7, 1, 4,  8, 0};
        int[] temp = new int[arr.length];
        MergeSort mergeSort = new MergeSort();
        mergeSort.sort(arr, 0, arr.length - 1, temp);
    }
}