package com.test.table;

/**
 * @author yslao@outlook.com
 * @since 2021/1/28
 */
public class EmpLinkedList {

    // 头节点,默认为null, 指向第一个对象
    private Emp head;

    /**
     * 添加员工,默认添加到链表最后
     * @param emp
     */
    public void add(Emp emp) {
        // 添加第一个,默认给head, 添加成功返回
        if (head == null) {
            head = emp;
            return;
        }
        Emp cur = head;
        while (true) {
            if (cur.getNext() == null) {
                break;
            }
            cur = cur.getNext();
        }
        cur.setNext(emp);
    }

    /**
     * 遍历
     */
    public void list(int index) {
        if (head == null) {
            System.out.println("表" + index + "为空");
            return;
        }
        System.out.print("表" + index + "的数据:");
        Emp cur = head;
        while (true) {
            System.out.printf("=> id:%d, name:%s\t", cur.getId(), cur.getName());
            if (cur.getNext() == null) {
                break;
            }
            cur = cur.getNext();
        }
        System.out.println();
    }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    public Emp findById(int id) {
        if (head == null) {
            System.out.println("findById表为空!");
            return null;
        }
        Emp cur = head;
        while (true) {
            // 找到
            if (cur.getId() == id) {
                break;
            }
            // 找到表最后还是没有找到
            if (cur.getNext() == null) {
                cur = null;
                break;
            }
            cur = cur.getNext();
        }
        return cur;
    }

    /**
     * 按照id删除
     * @param id
     */
    public void deleteById(int id) {
        if (head == null) {
            System.out.println("deleteById表为空");
            return;
        }
        // 判断head是不是要删除的节点
        if (head.getId() == id) {
            head = head.getNext();
            return;
        }
        Emp cur = head;
        while (true) {
            // 没有下一个节点,退出
            if (cur.getNext() == null) {
                System.out.println("表中不存在id:" + id);
                break;
            }
            if (cur.getNext().getId() == id) {
                cur.setNext(cur.getNext().getNext());
                break;
            }
            cur = cur.getNext();
        }
    }

}
