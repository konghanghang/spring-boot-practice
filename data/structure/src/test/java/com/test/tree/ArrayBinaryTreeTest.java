package com.test.tree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("顺序存储二叉树")
class ArrayBinaryTreeTest {

    private int[] arr = {1, 2, 3, 4, 5, 6, 7};

    private ArrayBinaryTree arrayBinaryTree = new ArrayBinaryTree(arr);

    @Test
    @DisplayName("递归")
    void orderRecursion() {
        System.out.println("前序：");// 1	2	4	5	3	6	7
        arrayBinaryTree.preOrderRecursion(0);
        System.out.println();
        System.out.println("中序：");// 4	2	5	1	6	3	7
        arrayBinaryTree.midOrderRecursion(0);
        System.out.println();
        System.out.println("后序：");// 4	5	2	6	7	3	1
        arrayBinaryTree.postOrderRecursion(0);
        System.out.println();
    }

    @Test
    @DisplayName("非递归")
    void orderNoRecursion() {
        System.out.println("前序：");// 1	2	4	5	3	6	7
        arrayBinaryTree.preOrderNoRecursion(0);
        System.out.println();
        System.out.println("中序：");// 4	2	5	1	6	3	7
        arrayBinaryTree.midOrderNoRecursion(0);
        System.out.println();
        System.out.println("后序：");// 4	5	2	6	7	3	1
        arrayBinaryTree.postOrderNoRecursion(0);
        System.out.println();
    }
}