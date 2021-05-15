package com.test.leetcode;

import org.junit.jupiter.api.Test;

/**
 * @author yslao@outlook.com
 * @since 2021/5/10
 */
class SortedArrayRemoveDuplicateTest {

    @Test
    void solution() {
        int[] nums = {1, 1, 2, 2, 3, 3, 4, 5};
        int solution = SortedArrayRemoveDuplicate.solution(nums);
        for (int i = 0; i < solution; i++) {
            System.out.println(nums[i]);
        }
    }
}