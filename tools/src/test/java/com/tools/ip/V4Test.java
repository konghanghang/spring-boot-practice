package com.tools.ip;

import com.tools.ip.ipv4.Ipv4Service;
import org.junit.Test;

public class V4Test {

    private String file = "D:\\project\\idea\\spring-boot-demo\\tools\\src\\main\\resources\\qqwry.dat";

    @Test
    public void load() throws Exception {
        Ipv4Service ipv4Service = Ipv4Service.getInstance();
        long ipNum = ipv4Service.ipv4ToNum("119.28.67.219");
        // 10357013  10357760
        long index = ipv4Service.find(ipNum);
        if(index != -1) {
            System.out.println(ipv4Service.readIPLocation((int) index));
        }
    }

}
