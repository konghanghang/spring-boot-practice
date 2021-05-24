package com.test;

public class Client {

    public  static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        //通过类名生成实例对象并将其返回
        ActorBuilder ab; //针对抽象建造者编程
        ab =  (ActorBuilder)Class.forName("com.test.AngelBuilder").newInstance(); //反射生成具体建造者对象

        ActorController ac = new  ActorController();
        Actor actor;
        actor = ac.construct(ab); //通过指挥者创建完整的建造者对象

        String  type = actor.getType();
        System.out.println(type  + "的外观：");
        System.out.println("性别：" + actor.getSex());
        System.out.println("面容：" + actor.getFace());
        System.out.println("服装：" + actor.getCostume());
        System.out.println("发型：" + actor.getHairstyle());
    }

}
