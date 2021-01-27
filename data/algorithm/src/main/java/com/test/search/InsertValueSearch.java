package com.test.search;

/**
 * 插值查找算法,是对二分查找的优化,只是在mid计算上进行优化,减少查找次数.
 * 同样要求待查找的数组是有序的
 * 公式: mid = left + (right - left) * (findValue - arr[left)) / (arr[right] - arr[left])
 * @author yslao@outlook.com
 * @since 2021/1/27
 */
public class InsertValueSearch {

    public int search(int[] arr, int left, int right, int value) {
        System.out.println("进行查找---");
        if (left > right || arr[left] > value || arr[right] < value) {
            return -1;
        }
        int mid = left + (right - left) * (value - arr[left]) / (arr[right] - arr[left]);
        if (value > arr[mid]) {
            // 向右查找
            return search(arr, mid + 1, right, value);
        } else if (value < arr[mid]) {
            // 向左查找
            return search(arr, left, mid - 1, value);
        } else {
            // 找到
            return mid;
        }
    }

}
