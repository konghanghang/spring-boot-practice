package com.test.tree;

import lombok.Data;

import java.util.Stack;

/**
 * 顺序存储二叉树
 * 1. 顺序二叉树一般只考虑完全二叉树
 * 2. 第n个节点的左子节点索引：2n + 1
 * 3. 第n个节点的右子节点索引：2n + 2
 * 4. 第n个节点的父节点索引：（n - 1）/ 2
 */
public class ArrayBinaryTree {

    private int[] arr;

    public ArrayBinaryTree(int[] arr) {
        this.arr = arr;
    }

    /**
     * 先序遍历递归调用
     * @param index
     */
    public void preOrderRecursion(int index) {
        if (arr == null || arr.length == 0) {
            System.out.println("数组为空！");
            return;
        }
        System.out.print(arr[index] + "\t");
        if ((2 * index + 1) < arr.length) {
            preOrderRecursion(2 * index + 1);
        }

        if ((2 * index + 2) < arr.length) {
            preOrderRecursion(2 * index + 2);
        }
    }

    /**
     * 先序遍历非递归调用
     * @param index
     */
    public void preOrderNoRecursion(int index) {
        if (arr == null || arr.length == 0) {
            System.out.println("数组为空！");
            return;
        }
        Stack<DataIndex> stack = new Stack<>();
        DataIndex dataIndex = new DataIndex(arr[index], index);
        stack.push(dataIndex);
        while (!stack.isEmpty()) {
            DataIndex pop = stack.pop();
            System.out.print(pop.getData() + "\t");
            int temp = pop.getIndex();
            // 把右子树放进栈
            if ((2 * temp + 2) < arr.length) {
                stack.push(new DataIndex(arr[2 * temp + 2], 2 * temp + 2));
            }
            // 把左子树放进栈
            if ((2 * temp + 1) < arr.length) {
                stack.push(new DataIndex(arr[2 * temp + 1], 2 * temp + 1));
            }
        }
    }

    /**
     * 中序遍历递归调用
     * @param index
     */
    public void midOrderRecursion(int index) {
        if (arr == null || arr.length == 0) {
            System.out.println("数组为空！");
            return;
        }
        if ((2 * index + 1) < arr.length) {
            midOrderRecursion(2 * index + 1);
        }
        System.out.print(arr[index] + "\t");
        if ((2 * index + 2) < arr.length) {
            midOrderRecursion(2 * index + 2);
        }
    }

    /**
     * 中序遍历非递归调用
     * @param index
     */
    public void midOrderNoRecursion(int index) {
        if (arr == null || arr.length == 0) {
            System.out.println("数组为空！");
            return;
        }
        Stack<DataIndex> stack = new Stack<>();
        DataIndex dataIndex = new DataIndex(arr[index], index);
        while (dataIndex != null || !stack.isEmpty()) {
            if (dataIndex != null) {
                stack.push(dataIndex);
                int nowIndex = dataIndex.getIndex();
                if ((2 * nowIndex + 1) < arr.length) {
                    dataIndex = new DataIndex(arr[2 * nowIndex + 1], 2 * nowIndex + 1);
                } else {
                    dataIndex = null;
                }
            } else {
                DataIndex pop = stack.pop();
                System.out.print(pop.getData() + "\t");
                int nowIndex = pop.getIndex();
                if ((2 * nowIndex + 2) < arr.length) {
                    dataIndex = new DataIndex(arr[2 * nowIndex + 2], 2 * nowIndex + 2);
                } else {
                    dataIndex = null;
                }
            }
        }
    }

    /**
     * 后序遍历递归调用
     * @param index
     */
    public void postOrderRecursion(int index) {
        if (arr == null || arr.length == 0) {
            System.out.println("数组为空！");
            return;
        }
        if ((2 * index + 1) < arr.length) {
            postOrderRecursion(2 * index + 1);
        }
        if ((2 * index + 2) < arr.length) {
            postOrderRecursion(2 * index + 2);
        }
        System.out.print(arr[index] + "\t");
    }

    /**
     * 后序遍历非递归调用
     * @param index
     */
    public void postOrderNoRecursion(int index) {
        if (arr == null || arr.length == 0) {
            System.out.println("数组为空！");
            return;
        }
        Stack<DataIndex> stack1 = new Stack<>();
        Stack<DataIndex> stack2 = new Stack<>();
        stack1.push(new DataIndex(arr[index], index));
        while (!stack1.isEmpty()) {
            DataIndex pop = stack1.pop();
            stack2.push(pop);
            int temp = pop.getIndex();
            // 把左子树放进栈
            if ((2 * temp + 1) < arr.length) {
                stack1.push(new DataIndex(arr[2 * temp + 1], 2 * temp + 1));
            }
            // 把右子树放进栈
            if ((2 * temp + 2) < arr.length) {
                stack1.push(new DataIndex(arr[2 * temp + 2], 2 * temp + 2));
            }
        }
        while (!stack2.isEmpty()) {
            DataIndex pop = stack2.pop();
            System.out.print(pop.getData() + "\t");
        }
    }

    @Data
    class DataIndex {
        // 数据值
        private int data;
        // 该数据值在arr中的索引
        private int index;

        public DataIndex(int data, int index) {
            this.data = data;
            this.index = index;
        }
    }
}
