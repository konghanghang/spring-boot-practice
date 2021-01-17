package com.test.stack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void calculator() {
        String expression = "70+10*6-2";
        Calculator calculator = new Calculator();
        int res = calculator.calculator(expression);
        System.out.printf("%s = %d", expression, res);
    }
}