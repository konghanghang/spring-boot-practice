package com.test.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yslao@outlook.com
 * @since 2021/1/29
 */
@DisplayName("多叉树")
class TreeTest {

    private TreeNode treeNode = new TreeNode(0);

    @BeforeEach
    void beforeEach() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        TreeNode treeNode4 = new TreeNode(4);
        TreeNode treeNode5 = new TreeNode(5);
        TreeNode treeNode6 = new TreeNode(6);
        treeNode.getNodes().add(treeNode1);
        treeNode.getNodes().add(treeNode2);
        treeNode.getNodes().add(treeNode3);
        treeNode3.getNodes().add(treeNode4);
        treeNode3.getNodes().add(treeNode5);
        treeNode1.getNodes().add(treeNode6);
    }

    @Test
    @DisplayName("深度优先")
    void dfs() {
        System.out.println("递归:");
        Tree.dfsRecursion(treeNode);
        System.out.println();
        System.out.println("非递归:");
        Tree.dfsNoRecursion(treeNode);
    }

    @Test
    @DisplayName("广度优先")
    void bfs() {
        System.out.println("递归:");
        List<TreeNode> list = new ArrayList<>();
        list.add(treeNode);
        Tree.bfsRecursion(list);
        System.out.println();
        System.out.println("非递归:");
        Tree.bfsNoRecursion(treeNode);
    }
}