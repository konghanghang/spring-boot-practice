package com.test.sort;

import java.util.Arrays;

/**
 * 冒泡排序
 * @author yslao@outlook.com
 * @since 2021/1/19
 */
public class BubbleSort {

    public void sort(int[] arr){
        int temp;
        // 每走一趟少进行一次排序,所以用for
        for (int i = 0; i < arr.length - 1; i++) {
            // flag进行优化,如果已是排序好的,就不需要再进行循环
            boolean flag = false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                // 进行顺序交换
                if (arr[j] > arr[j + 1]){
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = true;
                }
            }
            System.out.println(Arrays.toString(arr));
            if (!flag){
                break;
            }
        }
    }

}
