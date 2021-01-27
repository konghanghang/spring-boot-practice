package com.test.search;

import java.util.Arrays;

public class FibonacciSearch {

    /**
     * https://www.cnblogs.com/bethunebtj/p/4839576.html
     * 开始表中记录的个数为某个斐波那契数小1，即n=F(k)-1;
     * 表中的数据是F(k)-1个，使用mid值进行分割又用掉一个，那么剩下F(k)-2个
     * 正好分给两个子序列，每个子序列的个数分别是F(k-1)-1与F(k-2)-1
     * @param arr
     * @param value
     * @return
     */
    public int search(int[] arr, int value) {
        // 数组的长度
        int n = arr.length;
        int left = 0;
        int right = arr.length - 1;
        int mid = 0;
        int k = 0;//保存斐波那契分割数值下标
        int[] f = fib();
        //计算n位于斐波那契数列的位置
        while (n > f[k]) {
            k++;
        }
        // 开始表中记录的个数为某个斐波那契数小1，即n=F(k)-1;
        // 可能temp的长度要大于arr的长度， 那么得到的temp后边的位置值都为0，所以要补成arr数组的最后一个数值
        int[] temp = Arrays.copyOf(arr, f[k] - 1);
        for (int i = right + 1; i < temp.length; i++) {
            temp[i] = arr[right];
        }
        while (left <= right) {
            mid = left + f[k - 1] - 1;
            if (value < temp[mid]) {
                // 向左查找
                // low=mid+1说明待查找的元素在[low,mid-1]范围内，
                // k-=1 说明范围[low,mid-1]内的元素个数为F(k-1)-1个
                right = mid - 1;
                k--;
            } else if (value > temp[mid]) {
                // 向右查找
                // left=mid+1说明待查找的元素在[mid+1,right]范围内，
                // k-=2 说明范围[mid+1,right]内的元素个数为n-（F[k-1])= F[k]-1-F[k-1]=F[k]-F[k-1]-1=F[k-2]-1个
                left = mid + 1;
                k -= 2;
            } else {
                // 找到
                if (mid < n) {
                    // 若相等则说明mid即为查找到的位置
                    return mid;
                } else {
                    // 若mid>=n则说明是扩展的数值,返回n-1
                    return n - 1;
                }
            }
        }
        return -1;
    }

    /**
     * 生成斐波那契数列
     * @return
     */
    private int[] fib() {
        int maxSize = 20;
        int[] fib = new int[maxSize];
        fib[0] = 1;
        fib[1] = 1;
        for (int i = 2; i < maxSize; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        return fib;
    }

}
