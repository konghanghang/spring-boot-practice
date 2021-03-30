package com.test.tree.sort;

import com.test.tree.BinaryTreeNode;

import java.util.Stack;

/**
 * 二叉排序树
 * @author yslao@outlook.com
 * @since 2021/3/30
 */
public class BinarySortTree {

    private BinaryTreeNode root;

    public void add(BinaryTreeNode node) {
        if (root == null) {
            root = node;
        } else {
            BinaryTreeNode temp = root;
            while (temp != null) {
                if (node.getData() < temp.getData()) {
                    BinaryTreeNode left = temp.getLeft();
                    if (left != null) {
                        temp = left;
                    } else {
                        temp.setLeft(node);
                        temp = null;
                    }
                } else {
                    BinaryTreeNode right = temp.getRight();
                    if (right != null) {
                        temp = right;
                    } else {
                        temp.setRight(node);
                        temp = null;
                    }
                }
            }
        }
    }

    public void midOrder() {
        if (root == null) {
            return;
        }
        BinaryTreeNode temp = root;
        Stack<BinaryTreeNode> stack = new Stack<>();
        while (temp != null || !stack.isEmpty()) {
            if (temp != null) {
                stack.push(temp);
                temp = temp.getLeft();
            } else {
                BinaryTreeNode pop = stack.pop();
                System.out.print(pop.getData() + "\t");
                temp = pop.getRight();
            }
        }
    }

}
