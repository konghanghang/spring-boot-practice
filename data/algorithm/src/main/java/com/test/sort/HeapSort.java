package com.test.sort;

/**
 * 堆排序
 */
public class HeapSort {

    public void sort(int[] arr) {
        // 首先将待排序数组构建成一个大顶堆或者小顶堆
        // 从小到大构建大顶堆
        // 从大到小构建小顶堆
        for (int i = (arr.length / 2) - 1; i >= 0; i--) {
            adjustHeap(arr, i, arr.length);
        }

        int temp;
        // 将堆顶元素与与末尾元素进行交换，最大元素沉到数组末端
        for (int i = arr.length - 1; i >= 0; i--) {
            temp = arr[i];
            arr[i] = arr[0];
            arr[0] = temp;
            adjustHeap(arr, 0, i);
        }
    }

    /**
     * 调整数组
     * 从第一个非叶子节点开始，第一个非叶子节点 = (arr.length / 2) - 1
     * 从左到右，从下到上进行调整
     * @param arr
     * @param i 第i个元素
     * @param length 数组长度
     */
    private void adjustHeap(int arr[], int i, int length) {

        int temp = arr[i];

        // j为第i个节点的左子节点
        for (int j = 2 * i + 1; j < length; j = j * 2 + 1) {
            if (j + 1 < length && arr[j] < arr[j + 1]) {
                // j指向右子节点
                j++;
            }
            if (arr[j] > temp) {
                arr[i] = arr[j];
                i = j; // i指向j继续循环比较
            } else {
                break;//
            }
        }
        // 当for循环结束后，我们已经将i节点的为父节点的树的最大值，放到了最顶部（局部）
        arr[i] = temp;
    }

}
