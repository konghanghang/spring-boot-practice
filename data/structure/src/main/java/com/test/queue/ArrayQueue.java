package com.test.queue;

import java.util.Scanner;

/**
 * 普通队列，使用一次就不能使用了
 */
public class ArrayQueue {

    public static void main(String[] args) {
        boolean loop = true;
        Queue queue = new Queue(3);
        while (loop){
            System.out.println("请输入操作码：");
            Scanner scanner = new Scanner(System.in);
            char c = scanner.next().charAt(0);
            switch (c){
                case 'a':
                    System.out.println("请输入数字：");
                    int i = scanner.nextInt();
                    queue.addQueue(i);
                    break;
                case 'g':
                    int queue1 = queue.getQueue();
                    System.out.println("获取到的数据是" + queue1);
                    break;
                case 's':
                    queue.showQueue();
                    break;
                case 'e':
                    scanner.close();
                    loop = false;
                    System.out.println("退出");
                    break;
                default:
                    System.out.println("----");
            }
        }
    }

}

class Queue{
    private int maxSize;//队列最大长度
    private int front;//队头，指向第一个元素的前一个位置
    private int rear;//队尾，指向队尾元素的前一个位置
    private int[] array;//队列

    public Queue(int maxSize) {
        this.maxSize = maxSize;
        front = -1;
        rear = -1;
        array = new int[maxSize];
    }

    public boolean isFull(){
        return rear == maxSize - 1;
    }

    public boolean isEmpty(){
        return rear == front;
    }

    public void addQueue(int a){
        if (isFull()){
            System.out.println("full");
            return;
        }
        rear++;
        array[rear] = a;
    }

    public int getQueue(){
        if (isEmpty()){
            System.out.println("empty");
            return -1;
        }
        front++;
        return array[front];
    }

    public void showQueue(){
        for (int i = 0; i < array.length; i++) {
            System.out.printf("array[%d]=%d", i, array[i]);
            System.out.println();
        }
    }
}
