package com.test.tree;

import lombok.Data;

/**
 * @author yslao@outlook.com
 * @since 2021/1/29
 */
@Data
public class BinaryTreeNode {

    /**
     * 数据域
     */
    private int data;

    /**
     * 左节点
     */
    private BinaryTreeNode left;

    /**
     * 右节点
     */
    private BinaryTreeNode right;

    public BinaryTreeNode(int data) {
        this.data = data;
    }
}
