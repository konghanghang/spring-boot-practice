package com.test.tree.threaded;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("线索化")
class ThreadedTreeTest {

    private ThreadedTreeNode node = new ThreadedTreeNode(1, "111");
    private ThreadedTreeNode node1 = new ThreadedTreeNode(3, "333");
    private ThreadedTreeNode node2 = new ThreadedTreeNode(6, "666");
    private ThreadedTreeNode node3 = new ThreadedTreeNode(8, "888");
    private ThreadedTreeNode node4 = new ThreadedTreeNode(10, "10");
    private ThreadedTreeNode node5 = new ThreadedTreeNode(14, "14");

    @BeforeEach
    void beforeEach() {
        node.setLeft(node1);
        node.setRight(node2);

        node1.setLeft(node3);
        node1.setRight(node4);

        node2.setLeft(node5);
    }

    @Test
    @DisplayName("中序")
    void threadedNodes() {
        ThreadedTree threadedTree = new ThreadedTree();
        threadedTree.threadedNodes(node);

        // 前驱节点
        System.out.println(node4.getLeft().getName());
        // 后继节点
        System.out.println(node4.getRight().getName());
    }

    @Test
    @DisplayName("遍历线索化后的数据")
    void midOrderThreadedNodes() {
        ThreadedTree threadedTree = new ThreadedTree();
        threadedTree.threadedNodes(node);
        threadedTree.midOrderThreadedNodes(node);
    }

}