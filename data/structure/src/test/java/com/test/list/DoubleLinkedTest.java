package com.test.list;

import com.test.list.model.Hero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoubleLinkedTest {

    private Hero hero1;
    private Hero hero2;
    private Hero hero3;
    private Hero hero4;

    private DoubleLinked doubleLinked;

    @BeforeEach
    void beforeEach(){
        hero1 = new Hero(1, "a", "a1");
        hero2 = new Hero(2, "b", "b1");
        hero3 = new Hero(3, "c", "c1");
        hero4 = new Hero(4, "d", "d1");
        doubleLinked = new DoubleLinked();
    }

    @Test
    void add() {
        doubleLinked.add(hero1);
        doubleLinked.add(hero2);
        doubleLinked.add(hero4);
        doubleLinked.add(hero3);
        doubleLinked.list();
    }

    @Test
    void update() {
        doubleLinked.add(hero1);
        doubleLinked.add(hero2);
        doubleLinked.add(hero4);
        doubleLinked.add(hero3);
        System.out.println("--原始");
        doubleLinked.list();
        Hero hero = new Hero(3, "cccc", "cccc1");
        doubleLinked.update(hero);
        System.out.println("修改后：");
        doubleLinked.list();
    }

    @Test
    void delete() {
        doubleLinked.add(hero1);
        doubleLinked.add(hero2);
        doubleLinked.add(hero4);
        doubleLinked.add(hero3);
        System.out.println("--原始");
        doubleLinked.list();
        doubleLinked.delete(3);
        System.out.println("删除后：");
        doubleLinked.list();
        System.out.println("delete all");
        doubleLinked.delete(1);
        System.out.println("111");
        doubleLinked.delete(2);
        System.out.println("222");
        doubleLinked.delete(4);
        System.out.println("444");
        doubleLinked.list();
    }
}