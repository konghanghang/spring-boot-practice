package com.design.structual.bridge;

public class UnixImp implements ImageImp {
    @Override
    public void doPaint(Matrix matrix) {
        System.out.print("在Unix操作系统中显示图像：");
    }
}
