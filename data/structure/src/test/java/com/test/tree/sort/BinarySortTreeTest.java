package com.test.tree.sort;

import com.test.tree.BinaryTreeNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/3/30
 */
class BinarySortTreeTest {

    @DisplayName("构建排序二叉树")
    @Test
    void buildSortTree() {

        BinarySortTree binarySortTree = new BinarySortTree();
        int[] arr = {7, 3, 10, 12, 5, 1, 9};

        for (int i : arr) {
            binarySortTree.add(new BinaryTreeNode(i));
        }

        binarySortTree.midOrder();
    }
}