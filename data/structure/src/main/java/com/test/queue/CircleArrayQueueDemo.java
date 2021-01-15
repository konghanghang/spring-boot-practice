package com.test.queue;

import java.util.Scanner;

/**
 * 环形队列，可重复使用
 */
public class CircleArrayQueueDemo {

    public static void main(String[] args) {
        boolean loop = true;
        // 留了一个空位
        CircleArray queue = new CircleArray(4);
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

class CircleArray{
    private int maxSize;//队列最大长度
    private int front;//队头,指向队列的第一个元素
    private int rear;//队尾，指向最后一个元素的下一个位置
    private int[] array;//队列

    public CircleArray(int maxSize) {
        this.maxSize = maxSize;
        front = 0;
        rear = 0;
        array = new int[maxSize];
    }

    public boolean isFull(){
        return (rear + 1) % maxSize == front;
    }

    public boolean isEmpty(){
        return rear == front;
    }

    public void addQueue(int a){
        if (isFull()){
            System.out.println("full");
            return;
        }
        array[rear] = a;
        rear = (rear + 1) % maxSize;
    }

    public int getQueue(){
        if (isEmpty()){
            System.out.println("empty");
            return -1;
        }
        int i = array[front];
        front = (front + 1) % maxSize;
        return i;
    }

    public void showQueue(){
        for (int i = front; i < (front + size()); i++) {
            System.out.printf("array[%d]=%d", i % maxSize, array[i % maxSize]);
            System.out.println();
        }
    }

    public int size(){
        return (rear + maxSize -front) % maxSize;
    }
}