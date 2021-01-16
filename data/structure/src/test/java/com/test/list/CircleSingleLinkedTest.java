package com.test.list;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircleSingleLinkedTest {

    @Test
    void add() {
        CircleSingleLinked circleSingleLinked = new CircleSingleLinked();
        circleSingleLinked.add(5);
        circleSingleLinked.list();
    }

    @Test
    void outListTest(){
        CircleSingleLinked circleSingleLinked = new CircleSingleLinked();
        int size = 5;
        circleSingleLinked.add(size);
        circleSingleLinked.list();
        System.out.println("开始出圈");
        circleSingleLinked.outList(1, 2, size);
    }
}