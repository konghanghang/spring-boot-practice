package com.test.sort;

import java.util.Arrays;

/**
 * 选择排序
 * @author yslao@outlook.com
 * @since 2021/1/19
 */
public class SelectSort {

    public void sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;// 最小数的索引
            int min = arr[i];// 最小数的值
            for (int j = i + 1; j < arr.length; j++) {
                // 寻找最小值的下标和值
                if (min > arr[j]){
                    minIndex = j;
                    min = arr[j];
                }
            }
            if (minIndex != i){
                // 因为已经记录了最小数的索引和值,
                // 所以先把最小数的索引处的值设置成最大值
                arr[minIndex] = arr[i];
                // 然后把最小值设置到当前比较的位置
                arr[i] = min;
            }
            System.out.println("第" + (i + 1) + "次排序结果:");
            System.out.println(Arrays.toString(arr));
        }
    }

}
