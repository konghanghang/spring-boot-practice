package com.test;

public class Client {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Searcher searcher = (Searcher) Class.forName("com.test.ProxySearcher").newInstance();
        String rs = searcher.DoSearch("杨过","测试");
        System.out.println(rs);
    }

}
