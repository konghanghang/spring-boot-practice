package com.test.tree.huffman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 哈夫曼树
 * @author yslao@outlook.com
 * @since 2021/3/29
 */
public class HuffManTree {

    public Node createTree(int[] arr) {
        // 将数组转成node数组
        List<Node> list = new ArrayList<>();
        for (int i : arr) {
            list.add(new Node(i));
        }
        while (list.size() > 1) {
            Collections.sort(list);
            System.out.println(list);
            Node leftNode = list.get(0);
            Node rightNode = list.get(1);
            Node parent = new Node(leftNode.getWeight() + rightNode.getWeight());
            parent.setLeft(leftNode);
            parent.setRight(rightNode);
            list.remove(leftNode);
            list.remove(rightNode);
            list.add(parent);
        }
        return list.get(0);
    }

}
