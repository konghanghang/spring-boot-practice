package com.test.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/1/27
 */
class SeqSearchTest {

    @Test
    void search() {

        int[] arr = {2, 3, 6, 8, 10, 20, 21, 40};
        SeqSearch search = new SeqSearch();
        System.out.println(search.search(arr, 22));

    }
}