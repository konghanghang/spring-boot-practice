package com.test.list;

import com.test.list.model.Hero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SingleLinkedTest {

    @BeforeEach
    void beforeEach(){

    }

    @Test
    void addTest() {
        Hero hero1 = new Hero(1, "a", "a1");
        Hero hero2 = new Hero(2, "b", "b1");
        Hero hero3 = new Hero(3, "c", "c1");
        Hero hero4 = new Hero(4, "d", "d1");
        SingleLinked singleLinked = new SingleLinked();
        singleLinked.add(hero1);
        singleLinked.add(hero2);
        singleLinked.add(hero3);
        singleLinked.add(hero4);
        singleLinked.list();
    }

    @Test
    void addByOrderTest(){
        Hero hero1 = new Hero(1, "a", "a1");
        Hero hero2 = new Hero(2, "b", "b1");
        Hero hero3 = new Hero(3, "c", "c1");
        Hero hero4 = new Hero(4, "d", "d1");
        SingleLinked singleLinked = new SingleLinked();
        singleLinked.addByOrder(hero1);
        singleLinked.addByOrder(hero4);
        singleLinked.addByOrder(hero3);
        singleLinked.addByOrder(hero2);
        singleLinked.addByOrder(hero1);
        singleLinked.list();
    }
}