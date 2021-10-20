package com.design.structual.proxy;

public class Client {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Searcher searcher = (Searcher) Class.forName("com.design.structual.proxy.ProxySearcher").newInstance();
        String rs = searcher.DoSearch("杨过","测试");
        System.out.println(rs);
    }

}
