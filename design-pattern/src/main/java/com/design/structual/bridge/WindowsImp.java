package com.design.structual.bridge;

public class WindowsImp implements ImageImp {
    @Override
    public void doPaint(Matrix matrix) {
        System.out.print("在Windows操作系统中显示图像：");
    }
}
