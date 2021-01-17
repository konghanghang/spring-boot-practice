package com.test.stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("栈")
class ArrayStackTest {

    private ArrayStack arrayStack;

    @BeforeEach
    void beforeEach(){
        arrayStack = new ArrayStack(5);
        arrayStack.push(1);
        arrayStack.push(2);
        arrayStack.push(3);
    }

    @DisplayName("push")
    @Test
    void push() {
        arrayStack.push(4);
        arrayStack.push(4);
        arrayStack.push(4);
        arrayStack.list();
    }

    @DisplayName("pop")
    @Test
    void pop() {
        System.out.println(arrayStack.pop());
        System.out.println("-----");
        arrayStack.list();
        System.out.println(arrayStack.pop());
        System.out.println("-----");
        arrayStack.list();
        System.out.println(arrayStack.pop());
        System.out.println("-----");
        arrayStack.list();
        System.out.println(arrayStack.pop());
        System.out.println("-----");
        arrayStack.list();
    }
}