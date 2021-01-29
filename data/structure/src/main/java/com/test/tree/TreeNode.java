package com.test.tree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yslao@outlook.com
 * @since 2021/1/29
 */
@Data
public class TreeNode {

    /**
     * 数据域
     */
    private int data;

    /**
     * 子节点
     */
    private List<TreeNode> nodes = new ArrayList<>();

    public TreeNode(int data) {
        this.data = data;
    }
}
