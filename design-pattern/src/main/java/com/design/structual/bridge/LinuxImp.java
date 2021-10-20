package com.design.structual.bridge;

public class LinuxImp implements ImageImp {
    @Override
    public void doPaint(Matrix matrix) {
        System.out.print("在Linux操作系统中显示图像：");
    }
}
