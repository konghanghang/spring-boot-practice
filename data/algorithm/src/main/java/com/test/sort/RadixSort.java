package com.test.sort;

/**
 * 基数排序
 * @author yslao@outlook.com
 * @since 2021/1/27
 */
public class RadixSort {

    public void sort(int[] arr) {
        // 先定义桶
        int[][] bucket = new int[10][arr.length];
        // 桶中当前索引位置
        int[] bucketIndex = new int[10];
        // 当前数组中最大的数
        int max = arr[0];
        for (int i = 1; i < arr.length - 1; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        // 最大的数有多少位
        int length = (max + "").length();

        for (int i = 0, n = 1; i < length; i++, n *= 10) {
            // 将arr中的数据放到桶中
            for (int j = 0; j < arr.length; j++) {
                // 分别获取arr[i] 个位 十位 百位 ...的数字
                int index = arr[j] / n % 10;
                bucket[index][bucketIndex[index]] = arr[j];
                bucketIndex[index] += 1;
            }
            // 将桶中的数据取出
            int index = 0; // 原数组的下标
            for (int j = 0; j < bucket.length; j++) {
                int z = bucketIndex[j];
                if (z != 0) {
                    // 如果改桶的索引不为0,则说明桶中有数据,将数据取出放到arr中
                    for (int k = 0; k < z; k++) {
                        arr[index] = bucket[j][k];
                        index++;
                    }
                }
                // 取出数据后,将原桶的数据索引置为0
                bucketIndex[j] = 0;
            }
        }
    }

}
