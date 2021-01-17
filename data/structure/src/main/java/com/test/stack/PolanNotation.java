package com.test.stack;

import java.util.ArrayList;
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
        return cal(list);
    }

    /**
     * 计算
     * @param list 逆波兰表达式拆分开的集合
     * @return
     */
    public int cal(List<String> list){
        Stack<String> stack = new Stack<>();
        for (String item : list) {
            // 如果是数字放入栈中
            if (item.matches("\\d+")){
                stack.push(item);
            } else {
                // 否则进行弹出计算
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

    /**
     * 中缀表达式转list
     * @param expression
     * @return
     */
    public List<String> toInfixExpression(String expression){
        List<String> list = new ArrayList<>();
        int index = 0;
        char ch;
        while (index < expression.length()){
            // 如果不是数字直接加入到list中
            if ((ch = expression.charAt(index)) < 48 || (ch = expression.charAt(index)) > 57) {
                list.add(ch + "");
                index++;
            } else {
                // '0' = 48  '9' = 57
                String str = "";
                while (index < expression.length() &&
                        ((ch = expression.charAt(index)) >= 48 && (ch = expression.charAt(index)) <= 57)) {
                    str = str + ch;
                    index++;
                }
                list.add(str);
            }
        }
        return list;
    }

    /**
     * 中缀表达式list转后缀表达式
     * @param list
     * @return
     */
    public List<String> infixList2SuffixExpress(List<String> list){
        // 按需求定义两个栈，但是其中有一个栈基本没有出栈操作，一直在入栈，所以定义一个list
        Stack<String> stack = new Stack<>();
        List<String> result = new ArrayList<>();
        for (String item : list) {
            if (item.matches("\\d+")){
                result.add(item);
            } else if ("(".equals(item)){
                // 如果是左括号直接入栈
                stack.push(item);
            } else if (")".equals(item)){
                // 如果遇到右括号，则将栈中的元素弹出加入到result中，直到遇到左括号，弹出左括号，不添加到result
                while (!stack.peek().equals("(")){
                    result.add(stack.pop());
                }
                stack.pop();
            } else {
                // 当item的优先级小于等于stack中的第一个元素时，将stack中的第一个元素pop出加入到result中
                // 然后循环比较item和此时stack中的第一个元素
                // '('左括号的优先级是-1 会小于当前的item运算符，所以只会执行将item压入栈顶的方法：stack.push(item);
                while (stack.size() != 0 && this.priority(item) <= this.priority(stack.peek())) {
                    result.add(stack.pop());
                }
                // 然后将item放入stack中
                stack.push(item);
            }
        }
        // 循环完后，把stack中的元素依次加入到result中
        while (stack.size() != 0){
            result.add(stack.pop());
        }
        return result;
    }

    /**
     * 根据运算符判断优先级,返回结果越大优先级越高
     * @param opera
     * @return
     */
    private int priority(String opera){
        if (opera.equals("*") || opera.equals("/")){
            return 1;
        } else if (opera.equals("-") || opera.equals("+")){
            return 0;
        } else {
            // 这里返回-1也很巧妙的处理了栈中有'('左括号的问题
            System.out.println("运算符有误");
            return -1;
        }
    }
}
