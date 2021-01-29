package com.test.tree;

import java.util.Stack;

/**
 * https://blog.csdn.net/gaoweijiegwj/article/details/105941422
 * @author yslao@outlook.com
 * @since 2021/1/29
 */
public class BinaryTree {

    /**
     * 先序遍历递归方式
     * @param treeNode
     */
    public static void preOrderRecursion(BinaryTreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        System.out.print(treeNode.getData() + "\t");
        // 向左递归
        preOrderRecursion(treeNode.getLeft());
        // 向右递归
        preOrderRecursion(treeNode.getRight());
    }

    /**
     * 先序遍历非递归方式
     * @param treeNode
     */
    public static void preOrderNoRecursion(BinaryTreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        Stack<BinaryTreeNode> stack = new Stack<>();
        stack.push(treeNode);
        while (!stack.isEmpty()) {
            BinaryTreeNode pop = stack.pop();
            System.out.print(pop.getData() + "\t");
            // 添加右子树
            if (pop.getRight() != null) {
                stack.push(pop.getRight());
            }
            // 添加左子树
            if (pop.getLeft() != null) {
                stack.push(pop.getLeft());
            }
        }
    }

    /**
     * 中序遍历递归方式
     * @param treeNode
     */
    public static void midOrderRecursion(BinaryTreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        // 向左递归
        midOrderRecursion(treeNode.getLeft());
        System.out.print(treeNode.getData() + "\t");
        // 向右递归
        midOrderRecursion(treeNode.getRight());
    }

    /**
     * 中序遍历非递归方式
     * @param treeNode
     */
    public static void midOrderNoRecursion(BinaryTreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        Stack<BinaryTreeNode> stack = new Stack<>();
        while (treeNode != null || !stack.isEmpty()) {
            if (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.getLeft();
            } else {
                BinaryTreeNode pop = stack.pop();
                System.out.print(pop.getData() + "\t");
                treeNode = pop.getRight();
            }
        }
    }

    /**
     * 后序遍历递归方式
     * @param treeNode
     */
    public static void postOrderRecursion(BinaryTreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        // 向左递归
        postOrderRecursion(treeNode.getLeft());
        // 向右递归
        postOrderRecursion(treeNode.getRight());
        System.out.print(treeNode.getData() + "\t");
    }

    /**
     * 后序遍历非递归方式
     * @param treeNode
     */
    public static void postOrderNoRecursion(BinaryTreeNode treeNode) {
        Stack<BinaryTreeNode> stack1 = new Stack<>();
        Stack<BinaryTreeNode> stack2 = new Stack<>();
        stack1.push(treeNode);
        while (!stack1.isEmpty()) {
            BinaryTreeNode pop = stack1.pop();
            stack2.push(pop);
            if (pop.getLeft() != null) {
                stack1.push(pop.getLeft());
            }
            if (pop.getRight() != null) {
                stack1.push(pop.getRight());
            }
        }
        while (!stack2.isEmpty()) {
            BinaryTreeNode pop = stack2.pop();
            System.out.print(pop.getData() + "\t");
        }
    }

}
