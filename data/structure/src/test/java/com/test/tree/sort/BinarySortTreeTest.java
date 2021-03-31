package com.test.tree.sort;

import com.test.tree.BinaryTree;
import com.test.tree.BinaryTreeNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
    }

    /**
     * 下边几个方法的树形状如下：
     *              7
     *            |    \
     *          3       10
     *        |  \     |  \
     *       1    5   9    12
     *        \
     *         2
     */
    @DisplayName("查找排序二叉树节点")
    @Test
    void searchSortTree() {
        BinarySortTree binarySortTree = new BinarySortTree();
        int[] arr = {7, 3, 10, 12, 5, 1, 9, 2};

        for (int i : arr) {
            binarySortTree.add(new BinaryTreeNode(i));
        }

        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());

        int value = 1;
        BinaryTreeNode search = binarySortTree.search(value);
        System.out.println();
        System.out.println("查找到待删除节点：");
        System.out.println(search);
        BinaryTreeNode parent = binarySortTree.searchParent(value);
        System.out.println("查找到父节点：");
        System.out.println(parent);
    }

    @DisplayName("删除排序二叉树节点")
    @Test
    void deleteSortTreeLeafNode() {
        BinarySortTree binarySortTree = new BinarySortTree();
        int[] arr = {7, 3, 10, 12, 5, 1, 9, 2};

        for (int i : arr) {
            binarySortTree.add(new BinaryTreeNode(i));
        }

        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
        System.out.println();
        // 删除叶子节点
        /*int value = 12;
        binarySortTree.deleteNode(value);*/
        // 删除只有一个子树的节点
        /*int value = 1;
        binarySortTree.deleteNode(value);*/
        // 删除有两个子树的节点
        /*int value = 3;
        binarySortTree.deleteNode(value);*/
        // 乱序删除
        binarySortTree.deleteNode(2);
        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
        binarySortTree.deleteNode(5);
        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
        binarySortTree.deleteNode(7);
        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
        binarySortTree.deleteNode(12);
        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
        binarySortTree.deleteNode(10);
        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
        binarySortTree.deleteNode(3);
        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
        binarySortTree.deleteNode(9);
        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
        binarySortTree.deleteNode(1);
        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
    }

    @Test
    @DisplayName("平衡二叉树测试")
    void AVLTree(){
        BinarySortTree binarySortTree = new BinarySortTree();
        // 左旋测试arr
        // int[] arr = {4, 3, 6, 5, 7, 8};
        // 右旋测试arr
        int[] arr = {10, 12, 8, 9, 7, 6};
        for (int i : arr) {
            binarySortTree.add(new BinaryTreeNode(i));
        }
        BinaryTree.midOrderNoRecursion(binarySortTree.getRoot());
        System.out.println("树的高度:" + BinaryTree.getHeightNoRecursion(binarySortTree.getRoot()));
        // 把add方法中的平衡方法注视掉进行测试未平衡之前
        // 恢复add方法中的平衡方法进行测试平衡之后
        System.out.println("树的左子树高度:" + BinaryTree.getHeightNoRecursion(binarySortTree.getRoot().getLeft()));
        System.out.println("树的右子树高度:" + BinaryTree.getHeightNoRecursion(binarySortTree.getRoot().getRight()));

    }
}