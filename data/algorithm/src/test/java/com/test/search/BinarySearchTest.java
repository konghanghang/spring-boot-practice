package com.test.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author yslao@outlook.com
 * @since 2021/1/27
 */
class BinarySearchTest {

    private BinarySearch binarySearch;

    @BeforeEach
    void init() {
        binarySearch = new BinarySearch();
    }

    @DisplayName("二分查找递归实现")
    @Test
    void searchOne() {
        int[] arr = {1, 2, 3, 4, 5, 5, 5, 5, 6, 7, 8, 9};
        System.out.println(binarySearch.searchOne(arr, 0, arr.length - 1, 5));
        System.out.println(binarySearch.searchMultiple(arr, 0, arr.length - 1, 5));
    }

    @DisplayName("二分查找非递归实现")
    @Test
    void searchNoRecursion() {
        int[] arr = {1, 3, 8, 10, 11, 67, 100};
        System.out.println(binarySearch.searchNoRecursion(arr, 11));
    }
}