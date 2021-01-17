package com.test.stack;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * 逆波兰表达式计算
 * 3 4 + 5 * 6 -
 * 从左向右扫描，如果是数字，放进栈中
 * 如果遇到运算符，则从栈中pop出两个数字进行运算，这里需要注意减法和除法，应该是栈顶的下一个元素减去或除以栈顶元素
 * 运算结果再放进栈中
 * 最后栈中只会有一个结果，就是运算结果
 */
public class PolanNotation {

    /**
     * 计算
     * @param expression 逆波兰表达式，中间空格隔开
     * @return
     */
    public int cal(String expression){
        String[] s = expression.split(" ");
        List<String> list = Arrays.asList(s);
        Stack<String> stack = new Stack<>();
        for (String item : list) {
            if (item.matches("\\d+")){
                stack.push(item);
            } else {
                int num1 = Integer.valueOf(stack.pop());
                int num2 = Integer.valueOf(stack.pop());
                int res = 0;
                if ("+".equals(item)){
                    res = num1 + num2;
                } else if ("-".equals(item)){
                    res = num2 - num1;
                } else if ("*".equals(item)){
                    res = num1 * num2;
                } else if ("/".equals(item)){
                    res = num2 / num1;
                } else {
                    throw new RuntimeException("符号不正确！");
                }
                stack.push(res + "");
            }
        }
        // 最后栈中只后留一个数据，就是结果
        return Integer.valueOf(stack.pop());
    }

}
