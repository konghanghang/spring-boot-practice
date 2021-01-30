package com.test.tree.threaded;

import lombok.Data;

/**
 * 线索化二叉树
 * left 指向左节点，或者指向前驱节点
 * right 指向右节点，或者指向后继节点
 */
@Data
public class ThreadedTreeNode {

    private int id;

    private String name;

    private ThreadedTreeNode left;

    private ThreadedTreeNode right;

    /**
     * 0：指向左节点  1：指向前驱节点
     */
    private int leftType;

    /**
     * 0：指向右节点 1：指向后级节点
     */
    private int rightType;

    public ThreadedTreeNode(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
