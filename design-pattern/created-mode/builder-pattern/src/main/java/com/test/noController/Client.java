package com.test.noController;


import com.test.Actor;

public class Client {

    public  static void main(String args[]){
        ActorBuilder ab; //针对抽象建造者编程
        //更改各个角色的builder extends noController.ActorBuilder即可成功运行 反射生成具体建造者对象
        ab =  null;//(ActorBuilder)XMLUtil.getBean();

        Actor actor;
        //通过指挥者创建完整的建造者对象
        actor = ab.construct();

        String  type = actor.getType();
        System.out.println(type  + "的外观：");
        System.out.println("性别：" + actor.getSex());
        System.out.println("面容：" + actor.getFace());
        System.out.println("服装：" + actor.getCostume());
        System.out.println("发型：" + actor.getHairstyle());
    }

}
