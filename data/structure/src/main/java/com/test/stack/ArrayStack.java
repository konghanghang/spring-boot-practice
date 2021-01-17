package com.test.stack;

import lombok.Data;

/**
 * 此方法使用的是数组实现
 * 如果使用链表来实现：
 * 则使用链表头插法，每次入栈则插入链表数据为头节点的下一个节点，然后出栈则删除链表头节点的下一个节点
 */
@Data
public class ArrayStack {

    private int maxSize; // 栈的大小

    private int[] stack;

    private int top = -1; // 栈顶，默认为-1

    public ArrayStack(int maxSize) {
        this.maxSize = maxSize;
        stack = new int[maxSize];
    }

    /**
     * 判断栈满
     * @return
     */
    public boolean isFull(){
        return top == maxSize - 1;
    }

    /**
     * 判断栈空
     * @return
     */
    public boolean isEmpty(){
        return top == -1;
    }

    /**
     * 入栈
     * @param num
     */
    public void  push(int num){
        if (isFull()){
            System.out.println("栈满");
            return;
        }
        stack[++top] = num;
    }

    /**
     * 出栈
     */
    public int pop(){
        if (isEmpty()){
            System.out.println("栈空");
            return -1;
        }
        return stack[top--];
    }

    /**
     * 打印栈中元素
     */
    public void list(){
        if (isEmpty()){
            System.out.println("list->栈空");
        }
        for (int i = top; i >= 0; i--) {
            System.out.printf("stack[%d]=%d\n", i, stack[i]);
        }
    }
}
