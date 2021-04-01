package com.test.search;

import java.util.ArrayList;
import java.util.List;

/**
 * 二分查找算法
 * 该算法要求待查找的数组是有序的
 * @author yslao@outlook.com
 * @since 2021/1/27
 */
public class BinarySearch {

    /**
     * 查找第一个
     * @param arr
     * @param left
     * @param right
     * @param value
     * @return
     */
    public int searchOne(int[] arr, int left, int right, int value) {
        if (left > right) {
            return -1;
        }
        int mid = (left + right) / 2;
        if (value > arr[mid]) {
            // 向右查找
            return searchOne(arr, mid + 1, right, value);
        } else if (value < arr[mid]) {
            // 向左查找
            return searchOne(arr, left, mid - 1, value);
        } else {
            // 找到
            return mid;
        }
    }

    /**
     * 查找如果有多个相同的值
     * @param arr
     * @param left
     * @param right
     * @param value
     * @return
     */
    public List<Integer> searchMultiple(int[] arr, int left, int right, int value) {
        if (left > right) {
            return new ArrayList<>();
        }
        int mid = (left + right) / 2;
        if (value > arr[mid]) {
            // 向右查找
            return searchMultiple(arr, mid + 1, right, value);
        } else if (value < arr[mid]) {
            // 向左查找
            return searchMultiple(arr, left, mid - 1, value);
        } else {
            // 找到
            List<Integer> list = new ArrayList<>();
            list.add(mid);
            int i = mid - 1;
            // 向左查询
            while (true) {
                if (arr[i] != arr[mid]) {
                    break;
                }
                list.add(i);
                i--;
            }
            // 向右查询
            int j = mid + 1;
            while (true) {
                if (arr[j] != arr[mid]) {
                    break;
                }
                list.add(j);
                j++;
            }
            return list;
        }
    }

    /**
     * 二分查找非递归实现
     * @param arr
     * @param value
     * @return
     */
    public int searchNoRecursion(int[] arr, int value) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] == value) {
                return mid;
            } else if (arr[mid] > value) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

}
