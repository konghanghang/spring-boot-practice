package com.test.search;

import org.junit.jupiter.api.Test;

class FibonacciSearchTest {

    @Test
    void search() {
        int[] arr = {1, 8, 10, 89, 100, 103, 456, 457, 690, 790, 1000, 1234};
        FibonacciSearch search = new FibonacciSearch();
        System.out.println(search.search(arr, 1));
    }
}