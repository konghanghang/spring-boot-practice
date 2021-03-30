package com.test.tree.huffman;

import com.test.tree.huffman.code.HuffmanCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        // 获取哈夫曼编码
        Map<Byte, String> codes = huffmanCode.getCodes(root);
        System.out.println(codes);
        // 进行压缩
        byte[] zip = huffmanCode.zip(strByte);
        System.out.println("哈夫曼树编码后的byte数组：" + Arrays.toString(zip));
        byte[] result = huffmanCode.unzip(zip);
        System.out.println("原字符串：" + new String(result));
    }

    @DisplayName("利用哈夫曼编码压缩文件")
    @Test
    void zipFile() {
        String src = "/Users/konghang/010616202068.pdf";
        String dist = "/Users/konghang/010616202068.kong";
        HuffmanCode code = new HuffmanCode();
        code.zipFile(src, dist);
        System.out.println("压缩完成！");
    }

    @DisplayName("解压压缩的文件")
    @Test
    void unzipFile() {
        String zip = "/Users/konghang/010616202068.kong";
        String dist = "/Users/konghang/test.pdf";
        HuffmanCode code = new HuffmanCode();
        code.unzipFile(zip, dist);
        System.out.println("解压完成！");
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