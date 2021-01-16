package com.test.list;

import com.test.list.model.Hero;

public class SingleLinked {

    // 创建头节点
    private Hero head = new Hero(0, "", "");

    public void list(){
        if (head.getNext() ==  null){
            System.out.println("表为空");
            return;
        }
        Hero temp = head.getNext();
        while (true){
            if (temp == null){
                break;
            }
            System.out.println(temp);
            temp = temp.getNext();
        }
    }

    /**
     * 直接添加到下一个位置
     * @param hero
     */
    public void add(Hero hero){
        Hero temp = head;
        while (true){
            if (temp.getNext() == null){
                // temp.setNext(hero);
                break;
            }
            // 下一个域不为空，则temp向下移
            temp = temp.getNext();
        }
        temp.setNext(hero);
    }

    /**
     * 按照顺序进行添加
     * @param hero
     */
    public void addByOrder(Hero hero){
        Hero temp = head;
        boolean flag = false;
        while (true){
            if (temp.getNext() == null){
                break;
            }
            if (temp.getNext().getNo() > hero.getNo()){
                break;
            }
            // 编号已存在
            if (temp.getNext().getNo() == hero.getNo()){
                flag = true;
                break;
            }
            // 下一个域不为空，则temp向下移
            temp = temp.getNext();
        }
        if (flag){
            System.out.println("编号已存在");
        } else {
            hero.setNext(temp.getNext());
            temp.setNext(hero);
        }
    }

}
