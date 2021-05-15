package com.test.leetcode;

import org.junit.jupiter.api.Test;

/**
 * @author yslao@outlook.com
 * @since 2021/5/12
 */
class RemoveElementTest {

    @Test
    void remove() {

        int[] nums = {1, 2, 4, 2, 5};
        int remove = RemoveElement.remove(nums, 2);
        for (int i = 0; i < remove; i++) {
            System.out.println(nums[i]);
        }
    }
}