package com.test.sort;

/**
 * 快速排序
 */
public class QuickSort {

    public void sort(int[] arr, int left, int right) {
        int l = left;
        int r = right;
        // 找当前数组的中间的数作为基准，找到基准后，下边的操作就是把：
        // 基准值左边的数都要小于等于基准值，基准值右边的数都要大于等于基准值
        int mid = arr[(left + right)/2];
        System.out.println("基准：" + mid);
        int temp = 0;
        while (l < r) {
            // 从L开始 从左往右找大于mid的数的索引
            while (arr[l] < mid) {
                l += 1;
            }
            // 从r开始 从右往左找小于mid的数的索引
            while (arr[r] > mid) {
                r -= 1;
            }
            // 如果 l == r 说明他们找到了mid值所在的索引，已经没有可以比较的数了，所以退出while循环
            if (l == r) {
                break;
            }
            // 如果l == r 不成立，则开始交换l和r此时位置上的值
            temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
            // 如果l处的值等于mid的值，则把r向左移动一位，因为r索引处的元素现在一定是大于mid，下次比较防止while (arr[r] > mid)不成立，又拿拿着arr[r]的原值到temp代码处进行交换
            if (arr[l] == mid) {
                r -= 1;
            }
            // 如果r处的值等于mid的值，则把l向右移动一位，因为l索引处的元素现在一定是小于mid，下次比较防止while (arr[l] < mid)不成立，又拿拿着arr[l]的原值到temp代码处进行交换
            if (arr[r] == mid) {
                l += 1;
            }
            // 如果少了上边两个if，则while(l < r) 则会一直执行，死循环
            // 就是为了防止arr[l]和arr[r]处的数和mid相等，然后arr[l]和arr[r]一直交换位置，而不进行索引移位，造成死循环
        }
        // 最终会退出while循环 并且此时l >= r ， 将l向右移动一位 r向左移动一位
        // 可以说这里的代码一定会执行，应该不要if也是可以的
        if (l >= r) {
            l += 1;
            r -= 1;
        }
        // 本来l和r退出while (l < r)循环的时候都在5的位置
        // left           lr                  right
        // 4    3    2    5    11    7    9    10
        // left      r          l             right
        // 进行了上边的-和+操作后

        // 然后开始递归mid左边和右边的子集合
        // 递归有条件限制, 根据上图，left要<r   right > l
        if (left < r) {
            sort(arr, left, r);
        }
        if (right > l) {
            sort(arr, l, right);
        }
    }

}
