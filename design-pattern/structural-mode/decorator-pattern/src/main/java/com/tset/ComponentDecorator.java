package com.tset;

public class ComponentDecorator extends Component {

    //维持对抽象构件类型对象的引用
    private Component component;

    //注入抽象构件类型的对象
    public ComponentDecorator(Component component){
        this.component = component;
    }

    @Override
    public void display() {
        component.display();
    }
}
