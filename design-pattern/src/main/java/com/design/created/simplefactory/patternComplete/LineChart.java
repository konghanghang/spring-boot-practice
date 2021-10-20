package com.design.created.simplefactory.patternComplete;

//折线图类：具体产品类
public class LineChart implements Chart {

    public LineChart() {
        System.out.println("创建折线图！");
    }

    @Override
    public void display() {
        System.out.println("显示折线图！");
    }
}
