package com.tools.ip;

import com.tools.ip.ipv6.Ipv6Service;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

public class V6Test {

    /**
     * 2409:8754:2:1::d24c:4b55 中国广东省|中国移动政企专线
     * 2400:3200:baba::1 中国浙江省杭州市|阿里云计算有限公司
     * fd00:172:28:50::116 局域网|局部单播地址
     */
    @Test
    public void load() {
        Ipv6Service ipv6Service = Ipv6Service.getInstance();
        BigInteger ipNum = ipv6Service.ipv6ToNum("fd00:172:28:50::116");
        BigInteger ip = ipNum.shiftRight(64).and(new BigInteger("FFFFFFFFFFFFFFFF", 16));
        System.out.println(ip.toString());
        // 查找ip的索引偏移
        long findIp = ipv6Service.find(ip, 0, ipv6Service.getIndexCount());
        System.out.println(findIp);// 23846
        // 得到索引记录
        long ip_off = ipv6Service.getIpOff(findIp);
        long ip_rec_off = ipv6Service.getIpRecOff(ip_off);
        String addr = ipv6Service.getAddr(ip_rec_off);
        System.out.println(addr);
    }

    @Test
    public void longTest() {
        System.out.println(Long.MAX_VALUE);
        long l = 12 + Long.MAX_VALUE;
        System.out.println(l);
    }

}
