package com.test.leetcode;

/**
 * 删除有序数组中的重复项,数组已排好序
 * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array/
 * @author yslao@outlook.com
 * @since 2021/5/10
 */
public class SortedArrayRemoveDuplicate {

    public static int solution(int[] nums) {
        int a = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[a] == nums[i]) {
                continue;
            } else {
                a += 1;
                nums[a] = nums[i];
            }
        }
        return a + 1;
    }

}
