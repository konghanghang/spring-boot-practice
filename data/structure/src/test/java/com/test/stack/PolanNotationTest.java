package com.test.stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class PolanNotationTest {

    private PolanNotation polanNotation;

    @BeforeEach
    void beforeEach(){
        polanNotation = new PolanNotation();
    }

    @Test
    void cal() {
        // (3+4)*5-6 => 3 4 + 5 * 6 -
        // 4*5-8+60+8/2
        // String expression = "3 4 + 5 * 6 -";
        String expression = "4 5 * 8 - 60 + 8 2 / +";
        int res = polanNotation.cal(expression);
        System.out.printf("%s = %d", expression, res);
    }

    @Test
    @DisplayName("中缀表达式转list")
    void toInfixExpression(){
        String expression = "(3+4)*5-6";
        List<String> list = polanNotation.toInfixExpression(expression);
        System.out.println(list);
    }

    @Test
    @DisplayName("中缀表达式list转后缀表达式")
    void infixList2SuffixExpress(){
        String expression = "(3+4)*5-6";
        List<String> list = polanNotation.toInfixExpression(expression);
        System.out.println("list:" + list);
        list = polanNotation.infixList2SuffixExpress(list);
        System.out.println(list);
        expression = "1+((2+3)*4)-5";
        list = polanNotation.toInfixExpression(expression);
        System.out.println("list:" + list);
        list = polanNotation.infixList2SuffixExpress(list);
        System.out.println(list);
        System.out.println(polanNotation.cal(list));
    }
}