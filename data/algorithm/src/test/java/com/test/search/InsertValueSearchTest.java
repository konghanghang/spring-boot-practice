package com.test.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/1/27
 */
class InsertValueSearchTest {

    @Test
    void search() {
        int[] arr = {1, 2, 31, 41, 51, 61, 71, 81, 91};
        InsertValueSearch insertValueSearch = new InsertValueSearch();
        System.out.println(insertValueSearch.search(arr, 0, arr.length - 1, 2));
    }
}