package com.test.sort;

public class InsertSort {

    public void sort(int[] arr) {
        int insertVal = 0;
        int insertIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            insertVal = arr[i];
            insertIndex = i - 1;
            // insertIndex >= 0 保证找插入位置不越界
            // insertVal < arr[insertIndex] 表明待插入的数还没有找到正确位置
            while (insertIndex >= 0 && insertVal < arr[insertIndex]) {
                arr[insertIndex + 1] = arr[insertIndex];
                insertIndex--;
            }
            // 当while循环结束时，说明找到了插入位置 insertIndex + 1
            // 如果insertIndex + 1和i一样，则不用进行交换
            if (insertIndex + 1 != i) {
                arr[insertIndex + 1] = insertVal;
            }
        }
    }

}
