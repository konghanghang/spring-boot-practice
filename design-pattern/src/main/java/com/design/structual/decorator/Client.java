package com.design.structual.decorator;

public class Client {

    public static void main(String[] args) {
        Component component,componentSb;
        component = new Window();
        componentSb = new ScrollBarDecorator(component);
        componentSb.display();
        Component componentBB = new BlackBorderDecorator(componentSb);
        componentBB.display();
    }

}
