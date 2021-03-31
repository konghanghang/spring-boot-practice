package com.test.tree.sort;

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

        binarySortTree.midOrder();
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

        binarySortTree.midOrder();

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

        binarySortTree.midOrder();
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
        binarySortTree.midOrder();
        binarySortTree.deleteNode(5);
        binarySortTree.midOrder();
        binarySortTree.deleteNode(7);
        binarySortTree.midOrder();
        binarySortTree.deleteNode(12);
        binarySortTree.midOrder();
        binarySortTree.deleteNode(10);
        binarySortTree.midOrder();
        binarySortTree.deleteNode(3);
        binarySortTree.midOrder();
        binarySortTree.deleteNode(9);
        binarySortTree.midOrder();
        binarySortTree.deleteNode(1);
        binarySortTree.midOrder();
    }
}