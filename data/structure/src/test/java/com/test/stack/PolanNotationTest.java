package com.test.stack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolanNotationTest {

    @Test
    void cal() {
        // (3+4)*5-6 => 3 4 + 5 * 6 -
        // 4*5-8+60+8/2
        // String expression = "3 4 + 5 * 6 -";
        String expression = "4 5 * 8 - 60 + 8 2 / +";
        PolanNotation polanNotation = new PolanNotation();
        int res = polanNotation.cal(expression);
        System.out.printf("%s = %d", expression, res);
    }
}