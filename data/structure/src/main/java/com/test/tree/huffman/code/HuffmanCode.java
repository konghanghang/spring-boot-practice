package com.test.tree.huffman.code;

import com.test.tree.huffman.Node;

import java.util.*;

/**
 * @author yslao@outlook.com
 * @since 2021/3/29
 */
public class HuffmanCode {

    public List<Node> getNodes(byte[] bytes) {
        List<Node> nodes = new ArrayList<>();
        Map<Byte, Integer> map = new HashMap<>();
        for (Byte byt :bytes){
            Integer count = map.get(byt);
            if (count == null) {
                map.put(byt, 1);
            } else {
                map.put(byt, count + 1);
            }
        }
        // 生成node
        for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
            nodes.add(new Node(entry.getKey(), entry.getValue()));
        }
        return nodes;
    }

    /**
     * 创建哈夫曼树
     * @param nodes
     * @return
     */
    public Node createHuffmanTree(List<Node> nodes) {
        while (nodes.size() > 1) {
            Collections.sort(nodes);
            // 如果是倒序排,则要从后边取
            Node leftNode = nodes.get(0);
            Node rightNode = nodes.get(1);
            // 生成的节点只有权值, 没有data值
            Node parent = new Node(null, leftNode.getWeight() + rightNode.getWeight());
            parent.setLeft(leftNode);
            parent.setRight(rightNode);
            nodes.add(parent);

            // 删除节点
            nodes.remove(leftNode);
            nodes.remove(rightNode);
        }
        return nodes.get(0);
    }

}
