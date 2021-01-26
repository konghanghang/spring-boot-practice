package com.test.sort;

/**
 * @author yslao@outlook.com
 * @since 2021/1/26
 */
public class MergeSort {

    public void sort(int[] arr, int left, int right, int[] temp){
        if (left < right) {
            int mid = (left + right) / 2;
            // 向左拆分
            sort(arr, left, mid, temp);
            // 向右拆分
            System.out.println("left:" + left + " mid:" + mid + " right:" + right);
            sort(arr, mid + 1, right, temp);
            System.out.println("--left:" + left + " mid:" + mid + " right:" + right);
            this.merge(arr, left, mid, right, temp);
        }
    }

    public void merge(int[] arr, int left, int mid, int right, int[] temp){
        int t = 0;// temp的指针
        int i = left;
        int j = mid + 1;
        // 进行拷贝
        while (i <= mid && j <= right) {
            if (arr[i] >= arr[j]){
                temp[t] = arr[j];
                t += 1;
                j += 1;
            } else {
                temp[t] = arr[i];
                t += 1;
                i += 1;
            }
        }
        // 出了循环以后, 肯定最多只有一个数组有数据
        // 假设左边的有数据
        while (i <= mid) {
            temp[t] = arr[i];
            t += 1;
            i += 1;
        }
        // 假设右边有数据
        while (j <= right) {
            temp[t] = arr[j];
            t += 1;
            j += 1;
        }
        // 将temp拷贝到arr中
        t = 0;
        int tempLeft = left;
        while (tempLeft <= right) {
            arr[tempLeft] = temp[t];
            tempLeft += 1;
            t += 1;
        }
    }

}
