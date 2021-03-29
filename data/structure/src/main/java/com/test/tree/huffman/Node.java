package com.test.tree.huffman;

import lombok.Data;

/**
 * @author yslao@outlook.com
 * @since 2021/3/29
 */
@Data
public class Node implements Comparable<Node> {
    // 存放数据, 'a' => 97, ' ' => 32
    private Byte data;
    // 权值
    private int weight;
    // 左子节点
    private Node left;
    // 右子节点
    private Node right;

    public Node(int weight) {
        this.weight = weight;
    }

    public Node(Byte data, int weight) {
        this.data = data;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Node weight:" + this.weight + "-data:" + data;
    }

    @Override
    public int compareTo(Node o) {
        return this.weight - o.weight;
    }
}
