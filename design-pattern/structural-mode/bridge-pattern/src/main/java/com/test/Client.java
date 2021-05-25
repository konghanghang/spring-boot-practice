package com.test;

public class Client {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Image image;
        ImageImp imp;
        image = (Image)Class.forName("com.test.JPGImage").newInstance();
        imp = (ImageImp)Class.forName("com.test.WindowsImp").newInstance();
        image.setImageImp(imp);
        image.parseFile("小龙女");

    }

}
