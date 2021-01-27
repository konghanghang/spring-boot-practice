package com.test.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/1/27
 */
class BinarySearchTest {

    @Test
    void searchOne() {
        int[] arr = {1, 2, 3, 4, 5, 5, 5, 5, 6, 7, 8, 9};
        BinarySearch binarySearch = new BinarySearch();
        System.out.println(binarySearch.searchOne(arr, 0, arr.length - 1, 5));
        System.out.println(binarySearch.searchMultiple(arr, 0, arr.length - 1, 5));
    }
}