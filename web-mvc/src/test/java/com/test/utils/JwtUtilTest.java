package com.test.utils;

import org.junit.Test;

public class JwtUtilTest {

    @Test
    public void createToken() {
        String konghang = JwtUtil.createToken("konghang");
        System.out.println(konghang);
    }
}
