package com.test.tree.huffman;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yslao@outlook.com
 * @since 2021/3/29
 */
class HuffManTreeTest {

    @Test
    void createTree() {

        int[] arr = {13, 7, 8, 3, 29, 6, 1};
        HuffManTree tree = new HuffManTree();
        Node node = tree.createTree(arr);
        preOrder(node);
    }

    /**
     * 先序遍历
     * @param node
     */
    private void preOrder(Node node) {
        System.out.println(node.getValue());
        if (node.getLeft() != null) {
            preOrder(node.getLeft());
        }

        if (node.getRight() != null) {
            preOrder(node.getRight());
        }
    }
}