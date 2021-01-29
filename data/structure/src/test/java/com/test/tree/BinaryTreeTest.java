package com.test.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/1/29
 */
@DisplayName("二叉树")
class BinaryTreeTest {

    private BinaryTreeNode binaryTreeNode = new BinaryTreeNode(0);

    @BeforeEach
    void beforeEach() {
        BinaryTreeNode binaryTreeNode1 = new BinaryTreeNode(1);
        BinaryTreeNode binaryTreeNode2 = new BinaryTreeNode(2);
        BinaryTreeNode binaryTreeNode3 = new BinaryTreeNode(3);
        BinaryTreeNode binaryTreeNode4 = new BinaryTreeNode(4);
        BinaryTreeNode binaryTreeNode5 = new BinaryTreeNode(5);
        BinaryTreeNode binaryTreeNode6 = new BinaryTreeNode(6);
        BinaryTreeNode binaryTreeNode7 = new BinaryTreeNode(7);
        BinaryTreeNode binaryTreeNode8 = new BinaryTreeNode(8);
        BinaryTreeNode binaryTreeNode9 = new BinaryTreeNode(9);
        binaryTreeNode.setLeft(binaryTreeNode1);
        binaryTreeNode.setRight(binaryTreeNode2);

        binaryTreeNode1.setLeft(binaryTreeNode3);
        binaryTreeNode1.setRight(binaryTreeNode4);

        binaryTreeNode2.setLeft(binaryTreeNode5);
        binaryTreeNode2.setRight(binaryTreeNode6);

        binaryTreeNode3.setLeft(binaryTreeNode7);
        binaryTreeNode3.setRight(binaryTreeNode8);

        binaryTreeNode4.setRight(binaryTreeNode9);
    }

    @Test
    @DisplayName("前序遍历")
    void preOrder() {
        System.out.println("递归:");
        BinaryTree.preOrderRecursion(binaryTreeNode);
        System.out.println();
        System.out.println("非递归:");
        BinaryTree.preOrderNoRecursion(binaryTreeNode);
    }

    @Test
    @DisplayName("中序遍历")
    void midOrder() {
        System.out.println("递归:");
        BinaryTree.midOrderRecursion(binaryTreeNode);
        System.out.println();
        System.out.println("非递归:");
        BinaryTree.midOrderNoRecursion(binaryTreeNode);
    }

    @Test
    @DisplayName("后序遍历")
    void postOrder() {
        System.out.println("递归:");
        BinaryTree.postOrderRecursion(binaryTreeNode);
        System.out.println();
        System.out.println("非递归:");
        BinaryTree.postOrderNoRecursion(binaryTreeNode);
    }
}