package com.test.table;

/**
 * 自定义一个hashTab, 用取模来计算散列值.
 * @author yslao@outlook.com
 * @since 2021/1/28
 */
public class HashTab {

    private EmpLinkedList[] empLinkedArray;
    private int size;

    public HashTab(int size) {
        this.size = size;
        this.empLinkedArray = new EmpLinkedList[size];
        for (int i = 0; i < size; i++) {
            this.empLinkedArray[i] = new EmpLinkedList();
        }
    }

    /**
     * 添加
     * @param emp
     */
    public void add(Emp emp) {
        int index = hashFun(emp.getId());
        empLinkedArray[index].add(emp);
    }

    /**
     * 遍历hashTab
     */
    public void list() {
        for (int i = 0; i < empLinkedArray.length; i++) {
            empLinkedArray[i].list(i + 1);
        }
    }

    /**
     * 根据id进行查找
     * @param id
     * @return
     */
    public void findById(int id) {
        int index = hashFun(id);
        Emp emp = empLinkedArray[index].findById(id);
        if (emp != null) {
            System.out.println("在第" + (index + 1) + "条链表中找到,id = " + id + ", name = " + emp.getName());
        } else {
            System.out.println("在第" + (index + 1) + "条链表中找不到id:" + id);
        }
    }

    /**
     * 根据id删除数据
     * @param id
     */
    public void deleteById(int id) {
        int index = hashFun(id);
        empLinkedArray[index].deleteById(id);
    }

    /**
     * 根据id,判断该数据放到第几条链表
     * @param id
     * @return
     */
    private int hashFun(int id) {
        return id % size;
    }
}
