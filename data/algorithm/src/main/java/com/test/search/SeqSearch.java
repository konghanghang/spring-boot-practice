package com.test.search;

/**
 * 线性查找
 * 原数组无需有序,逐个遍历
 * @author yslao@outlook.com
 * @since 2021/1/27
 */
public class SeqSearch {

    public int search(int[] arr, int value) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                index = i;
                break;
            }
        }
        return index;
    }

}
