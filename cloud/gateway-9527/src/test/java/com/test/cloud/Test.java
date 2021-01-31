package com.test.cloud;

import java.time.ZonedDateTime;

public class Test {

    public static void main(String[] args) {
        // 2021-01-31T18:47:58.954+08:00[Asia/Shanghai]
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(zonedDateTime);
    }

}
