package com.test.tree.huffman;

import lombok.Data;

/**
 * @author yslao@outlook.com
 * @since 2021/3/29
 */
@Data
public class Node implements Comparable<Node> {
    // 权值
    private int value;
    // 左子节点
    private Node left;
    // 右子节点
    private Node right;

    public Node(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Node value:" + this.value;
    }

    @Override
    public int compareTo(Node o) {
        return this.value - o.value;
    }
}
