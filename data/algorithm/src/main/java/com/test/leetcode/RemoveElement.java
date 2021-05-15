package com.test.leetcode;

/**
 * 给定值，移除相关元素，返回剩下数组长度
 * https://leetcode-cn.com/problems/remove-element/
 * @author yslao@outlook.com
 * @since 2021/5/12
 */
public class RemoveElement {

    public static int remove(int[] nums, int value) {
        int low = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != value) {
                nums[low] = nums[i];
                low++;
            }
        }
        return low;
    }

}
