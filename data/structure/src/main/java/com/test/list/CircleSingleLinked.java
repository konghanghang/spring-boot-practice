package com.test.list;

import com.test.list.model.Item;

/**
 * 单向环形链表，
 * 约瑟夫问题
 */
public class CircleSingleLinked {

    private Item first;

    /**
     * 构建环
     * @param nums 环中有几个元素
     */
    public void add(int nums){
        if (nums < 1){
            System.out.println("元素个数要大于1");
            return;
        }
        Item cur = null;
        for (int i = 1; i <= nums; i++) {
            Item item = new Item(i);
            if (i == 1){
                first = item;
                first.setNext(first);
                cur = first;
            } else {
                cur.setNext(item);
                item.setNext(first);
                cur = item;
            }
        }
    }

    /**
     * 打印链表
     */
    public void list(){
        if (first == null){
            System.out.println("空链表");
            return;
        }
        Item cur = first;
        while (true){
            System.out.println("当前编号：" + cur.getNo());
            if (cur.getNext() == first){
                break;
            }
            cur = cur.getNext();
        }
    }

    /**
     * 出圈
     * @param startNo 从第几个开始数
     * @param countNo 数几下
     * @param nums 最初圈中有多少个数据
     */
    public void outList(int startNo, int countNo, int nums){
        if (first == null || startNo < 1 || startNo > nums){
            System.out.println("参数有问题");
            return;
        }
        // 先让helper指向最后一个节点
        Item helper = first;
        while (true){
            if (helper.getNext() == first){
                break;
            }
            helper = helper.getNext();
        }
        // 移动指针到从第几个开始的前一个(startNo - 1)
        for (int i = 0; i < startNo - 1; i++) {
            first = first.getNext();
            helper = helper.getNext();
        }
        // 让first 和 helper 移动countNo - 1 次 模拟报数
        while (true){
            if (helper == first){
                System.out.println(first.getNo());
                break;
            }
            // 移动
            for (int i = 0; i < countNo - 1; i++) {
                first = first.getNext();
                helper = helper.getNext();
            }
            // 出圈
            System.out.println("出圈编码：" + first.getNo());
            first = first.getNext();
            helper.setNext(first);
        }
    }

}
