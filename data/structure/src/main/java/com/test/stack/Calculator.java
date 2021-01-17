package com.test.stack;

public class Calculator {

    /**
     * 计算中缀表达式
     * @param expression
     * @return
     */
    public int calculator(String expression){
        int num1 = 0;
        int num2 = 0;
        int opera = 0;//运算符
        int index = 0;//用于扫描
        String keepNum = "";
        ArrayStack numStack = new ArrayStack(10);
        ArrayStack operaStack = new ArrayStack(10);
        char ch = ' ';
        while (true){
            ch = expression.substring(index, index + 1).charAt(0);
            // 如果是运算符
            if (this.isOpera(ch)){
                // 判断运算符栈中是否已有运算符
                if (!operaStack.isEmpty()){
                    // 如果不为空，则判断当前运算符和 运算符栈中的第一个运算符的优先级
                    if (this.priority(ch) <= this.priority(operaStack.peek())){
                        // 如果当前运算符的优先级小于等于运算符栈中的第一个运算符的优先级
                        // 则从opera栈中pop出一个运算符 num栈pop出两个数进行运算
                        // 然后把运算结果push进num栈，当前运算符push进opera栈
                        opera = operaStack.pop();
                        num1 = numStack.pop();
                        num2 = numStack.pop();
                        int res = this.cal(num1, num2, opera);
                        numStack.push(res);
                        // 当前运算符push进opera栈 ----------------------------三处代码一样，所以抽取出来
                    } else {
                        // 如果当前运算符的优先级大于运算符栈中的第一个运算符的优先级，
                        // 直接放入opera栈---------------------
                        // operaStack.push(ch);
                    }
                } else {
                    // 如果为空则直接把运算符放入运算符栈---------------------
                    // operaStack.push(ch);
                }
                // 抽取出来
                operaStack.push(ch);
            } else {
                // 如果是数字， 直接进入数字栈，这样子的代码只能计算个位数，
                // numStack.push(ch - '0');
                // 为了兼容多位数，要判断当前ch的下一位是不是运算符，如果是运算符则push，不是则进行下一次循环
                keepNum += ch;
                // 如果是最后一位就不用再判断了
                if (index == expression.length() - 1){
                    numStack.push(Integer.valueOf(keepNum));
                } else {
                    if (this.isOpera(expression.substring(index + 1, index + 2).charAt(0))) {
                        numStack.push(Integer.valueOf(keepNum));
                        keepNum = "";
                    }
                }
            }
            index++;
            if (index == expression.length()){
                break;
            }
        }
        // 继续计算，直到符号栈为空，取出num栈的栈顶元素则为结果
        while (!operaStack.isEmpty()){
            num1 = numStack.pop();
            num2 = numStack.pop();
            opera = operaStack.pop();
            int cal = this.cal(num1, num2, opera);
            numStack.push(cal);
        }
        return numStack.pop();
    }

    /**
     * 根据运算符判断优先级,返回结果越大优先级越高
     * @param opera
     * @return
     */
    private int priority(int opera){
        if (opera == '*' || opera == '/'){
            return 1;
        } else if (opera == '-' || opera == '+'){
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 判断是否是运算符
     * @param val
     * @return
     */
    private boolean isOpera(char val){
        return val == '+' || val == '-' || val == '*' || val == '/';
    }

    /**
     * 计算方法
     * @param num1
     * @param num2
     * @param opera
     * @return
     */
    private int cal(int num1, int num2, int opera){
        int res = 0;
        switch (opera) {
            case '+':
                res = num1 + num2;
                break;
            case '-':
                res = num2 - num1;
                break;
            case '*':
                res = num1 * num2;
                break;
            case '/':
                res = num2 / num1;
                break;
            default:
                break;
        }
        return res;
    }
}
