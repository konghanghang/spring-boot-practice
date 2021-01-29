package com.test.tree;

import java.util.*;

/**
 * 多叉树dfs和bfs
 * @author yslao@outlook.com
 * @since 2021/1/29
 */
public class Tree {

    /**
     * 深度优先递归调用
     * @param treeNode
     */
    public static void dfsRecursion(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        System.out.print(treeNode.getData() + "\t");
        for (TreeNode node : treeNode.getNodes()) {
            dfsRecursion(node);
        }
    }

    /**
     * 深度优先非递归调用
     * @param treeNode
     */
    public static void dfsNoRecursion(TreeNode treeNode) {
        Stack<TreeNode> stack = new Stack<>();
        stack.push(treeNode);
        while (stack.size() != 0) {
            TreeNode pop = stack.pop();
            System.out.print(pop.getData() + "\t");
            List<TreeNode> nodes = pop.getNodes();
            for (int i = nodes.size() - 1; i >= 0; i--) {
                stack.push(nodes.get(i));
            }
        }
    }

    /**
     * 广度优先递归调用
     * @param treeNode
     */
    public static void bfsRecursion(List<TreeNode> treeNode) {
        List<TreeNode> children = new ArrayList<>();
        for (TreeNode node : treeNode) {
            System.out.print(node.getData() + "\t");
            if (node.getNodes() != null && node.getNodes().size() > 0) {
                children.addAll(node.getNodes());
            }
        }
        if (children.size() > 0) {
            bfsRecursion(children);
        }
    }

    /**
     * 广度优先非递归调用
     * @param treeNode
     */
    public static void bfsNoRecursion(TreeNode treeNode) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(treeNode);
        while (queue.size() != 0) {
            TreeNode poll = queue.poll();
            System.out.print(poll.getData() + "\t");
            for (TreeNode node : poll.getNodes()) {
                queue.offer(node);
            }
        }
    }

}
