package com.test.tree.huffman;

import com.test.tree.huffman.code.HuffmanCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author yslao@outlook.com
 * @since 2021/3/29
 */
class HuffManTreeTest {

    @DisplayName("创建哈夫曼树")
    @Test
    void createTree() {

        int[] arr = {13, 7, 8, 3, 29, 6, 1};
        HuffManTree tree = new HuffManTree();
        Node node = tree.createTree(arr);
        preOrder(node);
    }

    @DisplayName("哈夫曼树编码")
    @Test
    void treeCode() {

        String str = "i like like like java do you like a java";
        byte[] strByte = str.getBytes(StandardCharsets.UTF_8);
        System.out.println(strByte.length);
        HuffmanCode huffmanCode = new HuffmanCode();
        // 哈夫曼节点
        List<Node> nodes = huffmanCode.getNodes(strByte);
        System.out.println(nodes);
        // 生成哈夫曼树
        Node root = huffmanCode.createHuffmanTree(nodes);
        preOrder(root);
    }

    /**
     * 先序遍历
     * @param node
     */
    private void preOrder(Node node) {
        System.out.println(node.getData() + "-" + node.getWeight());
        if (node.getLeft() != null) {
            preOrder(node.getLeft());
        }

        if (node.getRight() != null) {
            preOrder(node.getRight());
        }
    }
}