package com.test.sort;

/**
 * 希尔排序,对插入排序进行增强
 * @author yslao@outlook.com
 * @since 2021/1/21
 */
public class ShellSort {
    
    public void sort(int arr[]){
        // 进行分组,每次都是/2
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            // 分组后,从分组最大位置往后走
            for (int i = gap; i < arr.length; i++) {
                // 先记录下当前的值和索引
                int j = i;
                int value = arr[i];
                // 如果当前值小于它当前所在分组的前边的数
                if (arr[i] < arr[i - gap]) {
                    // 进行插入排序,移位,找到需要插入的位置,
                    // 这里进行j-gap判断,并没有赋值给j,所以下边arr[j] = value, 和插入排序的arr[j+1] = value有一点差别
                    while (j - gap >=0 && arr[j - gap] > value){
                        arr[j] = arr[j - gap];
                        j -= gap;
                    }
                    if (i != j){
                        arr[j] = value;
                    }
                }
            }
        }
    }
    
}
