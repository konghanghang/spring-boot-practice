package com.test.list;

import com.test.list.model.Hero;
import lombok.Getter;

public class DoubleLinked {

    @Getter
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
        hero.setPre(temp);
    }

    /**
     * 更新节点
     * @param hero
     */
    public void update(Hero hero){
        if (head.getNext() == null){
            System.out.println("表为空");
            return;
        }
        Hero temp = head.getNext();
        // 判断是否找到了对应编号的英雄
        boolean flag = false;
        while (true){
            if (temp == null){
                break;
            }
            if (temp.getNo() == hero.getNo()) {
                flag = true;
                break;
            }
            temp = temp.getNext();
        }
        if (flag){
            temp.setName(hero.getName());
            temp.setNickname(hero.getNickname());
        } else {
            System.out.println("未找到节点");
        }
    }

    /**
     * 删除节点
     * @param no
     */
    public void delete(int no){
        if (head.getNext() == null){
            System.out.println("链为空");
            return;
        }
        Hero temp = head.getNext();
        boolean flag = false;
        while (true){
            if (temp == null){
                break;
            }
            if (temp.getNo() == no){
                // 找到了
                flag = true;
                break;
            }
            temp = temp.getNext();
        }
        if (flag){
            temp.getPre().setNext(temp.getNext());
            if (temp.getNext() != null)
                temp.getNext().setPre(temp.getPre());
        } else {
            System.out.println("未找到");
        }
    }

}
