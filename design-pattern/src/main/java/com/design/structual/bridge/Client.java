package com.design.structual.bridge;

public class Client {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Image image;
        ImageImp imp;
        image = (Image)Class.forName("com.design.structual.bridge.JPGImage").newInstance();
        imp = (ImageImp)Class.forName("com.design.structual.bridge.WindowsImp").newInstance();
        image.setImageImp(imp);
        image.parseFile("小龙女");

    }

}
