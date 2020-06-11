package com.test.ip;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class IpTest {

    public static void main(String[] args) throws IOException {
        System.out.println(Long.MAX_VALUE);
        IpAddress instance = IpAddress.getInstance();
        byte[] bytes = instance.readBytes(0,4);
        System.out.println(new String(bytes));
        bytes = instance.readBytes(4, 2);
        System.out.println(ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort());
        bytes = instance.readBytes(6, 1);
        int offsetLen = bytes[0];
        System.out.println("偏移地址长度:" + offsetLen);
        bytes = instance.readBytes(7, 1);
        int size = bytes[0];
        // 8
        System.out.println("IP地址长度(4或8或12或16, 现在只支持4(ipv4)和8(ipv6)):" + size);
        bytes = instance.readBytes(8, 8);
        BigInteger indexCountBig = byteArrayToLong(bytes);
        long indexCount = indexCountBig.longValue();
        // 141389
        System.out.println("记录数:" + indexCount);
        bytes = instance.readBytes(16, 8);
        BigInteger firstIndexBig = byteArrayToLong(bytes);
        long firstIndex = firstIndexBig.longValue();
        // 570063
        System.out.println("索引区第一条记录的偏移:" + firstIndex);
        // 2001:da8:202:10::36
        BigInteger ipNum = ipv6ToNum("2409:8754:2:1::d24c:4b55");
        BigInteger ip = ipNum.shiftRight(64).and(new BigInteger("FFFFFFFFFFFFFFFF", 16));
        System.out.println(ip.toString());
        // 查找ip的索引偏移
        long findIp = find(ip, 0, indexCount, offsetLen, firstIndex, instance);
        // 23846
        System.out.println(findIp);
        // 得到索引记录
        long ip_off = firstIndex + findIp * (8 + offsetLen);
        bytes = instance.readBytes(ip_off + 8, offsetLen);
        BigInteger ip_rec_off_big = byteArrayToLong(bytes);
        // long ip_rec_off = bytesToLong(bytes);
        // 278917
        System.out.println(ip_rec_off_big.longValue());
        String addr = getAddr(ip_rec_off_big.longValue(), offsetLen, instance);
        bytes = instance.readBytes(ip_off, 8);
        BigInteger bigInteger = byteArrayToLong(bytes);
        long begin = byteToLong(bytes);
        String i1 = numToV6(begin);
        String i2;
        try {
            bytes = instance.readBytes(ip_off + 8 + offsetLen, 8);
            long end = byteToLong(bytes);
            i2 = numToV6(end - 1);
        } catch (Exception e) {
            i2 = "FFFF:FFFF:FFFF:FFFF::";
        }
        if (ipNum.intValue() == 0x1) {
            i1 = "0:0:0:0:0:0:0:1";
            i2 = "0:0:0:0:0:0:0:1";
            addr = "本机地址";
        }
        System.out.println(addr);
        System.out.println(i1);
        System.out.println(i2);
        long l = ipv4ToNum("192.168.13.24");
        System.out.println(new BigInteger(((l >> 16) & 0xFFFF) + "").toString(16));
        System.out.println(new BigInteger((l & 0xFFFF) + "").toString(16));
    }

    private static long find (BigInteger ip, long l, long r, int offsetLen, long firstIndex, IpAddress instance){
        if (r - l <= 1)
            return l;
        long m = (l + r) / 2;
        long o = firstIndex + m * (8 + offsetLen);
        byte[] bytes = instance.readBytes(o,  8);
        long l1 = byteToLong(bytes);
        //BigInteger new_ip = byteArrayToLong(bytes);
        BigInteger new_ip = new BigInteger(l1 + "");
        if (ip.compareTo(new_ip) == -1) {
            return find(ip, l, m, offsetLen, firstIndex, instance);
        } else {
            return find(ip, m, r, offsetLen, firstIndex, instance);
        }
    }

    private static String getAddr(long offset, int offsetLen, IpAddress instance) {
        byte[] bytes = instance.readBytes(offset, 1);
        int num = bytes[0];
        if (num == 1) {
            // 重定向模式1
			// [IP][0x01][国家和地区信息的绝对偏移地址]
            // 使用接下来的3字节作为偏移量调用字节取得信息
            bytes = instance.readBytes(offset + 1, offsetLen);
            BigInteger l = byteArrayToLong(bytes);
            return getAddr(l.longValue(), offsetLen, instance);
        } else {
            // 重定向模式2 + 正常模式
			// [IP][0x02][信息的绝对偏移][...]
            String cArea = getAreaAddr(offset, offsetLen, instance);
            if (num == 2) {
                offset += 1 + offsetLen;
            } else {
                offset = findEnd(offset, instance) + 1;
            }
            String aArea = getAreaAddr(offset, offsetLen, instance);
            return cArea + "|" + aArea;
        }
    }

    private static String getAreaAddr(long offset, int offsetLen, IpAddress instance){
        // 通过给出偏移值，取得区域信息字符串
        byte[] bytes = instance.readBytes(offset, 1);
        int num = bytes[0];
        if (num == 1 || num == 2) {
            bytes = instance.readBytes(offset + 1, offsetLen);
            BigInteger p = byteArrayToLong(bytes);
            return getAreaAddr(p.longValue(), offsetLen, instance);
        } else {
            return getString(offset, instance);
        }
    }

    private static String getString(long offset, IpAddress instance) {
        long o2 = findEnd(offset, instance);
		// 有可能只有国家信息没有地区信息，
        byte[] bytes = instance.readBytes(offset, Long.valueOf(o2 - offset).intValue());
        try {
            return new String(bytes, "utf8");
        } catch (UnsupportedEncodingException e) {
            return "未知数据";
        }
    }

    private static long findEnd(long offset, IpAddress instance) {
        int i = Long.valueOf(offset).intValue();
        for (; i < Long.MAX_VALUE; i++) {
            byte[] bytes = instance.readBytes(i, 1);
            if ("\0".equals(new String(bytes))) {
                break;
            }
        }
        return i;
    }

    public static BigInteger ipv6ToNum(String ipStr) {
        // String ipStr = "2400:3200::1";
        int ipCount = 8;
        String[] parts = ipStr.split(":");
        if (parts[parts.length - 1].contains(".")) {
            long l = ipv4ToNum(parts[parts.length - 1]);
            System.out.println((l >> 16) & 0xFFFF);
            System.out.println(l & 0xFFFF);
        }
        int emptyIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (StringUtils.isEmpty(parts[i])) {
                emptyIndex = i;
            }
        }
        int parts_hi, parts_lo, parts_skipped;
        if (emptyIndex > -1) {
            parts_hi = emptyIndex;
            parts_lo = parts.length - parts_hi - 1;
            if (StringUtils.isEmpty(parts[0])) {
                parts_hi -= 1 ;
                if (parts_hi > 0) {
                    System.out.println("error ip address");
                }
            }
            if (StringUtils.isEmpty(parts[parts.length - 1])) {
                parts_lo -= 1;
                if (parts_lo > 0) {
                    System.out.println("error ip address");
                }
            }
            parts_skipped = ipCount - parts_hi - parts_lo;
        } else {
            // 完全地址
            if (parts.length != ipCount) {
                System.out.println("error ip address");
            }
            parts_hi = parts.length;
            parts_lo = 0;
            parts_skipped = 0;
        }
        BigInteger ipNum = new BigInteger("0");
        if (parts_hi > 0) {
            for (int i = 0; i < parts_hi; i++) {
                ipNum = ipNum.shiftLeft(16);
                String part = parts[i];
                if (part.length() > 4) {
                    System.out.println("error ip address");
                }
                BigInteger bigInteger = new BigInteger(part, 16);
                int i1 = bigInteger.intValue();
                if (i1 > 0xFFFF) {
                    System.out.println("error ip address");
                }
                ipNum = ipNum.or(bigInteger);
            }
        }
        ipNum = ipNum.shiftLeft(16 * parts_skipped);
        for (int i = -parts_lo; i < 0; i++) {
            // ipNum <<= 16;
            ipNum = ipNum.shiftLeft(16);
            String part = parts[parts.length + i];
            if (part.length() > 4) {
                System.out.println("error ip address");
            }
            BigInteger bigInteger = new BigInteger(part, 16);
            int i1 = bigInteger.intValue();
            if (i1 > 0xFFFF) {
                System.out.println("error ip address");
            }
            // ipNum |= i1;
            ipNum = ipNum.or(bigInteger);
        }

        System.out.println(ipNum);
        return ipNum;
    }

    public static long ipv4ToNum(String ipStr) {
        long result = 0;
        String[] split = ipStr.split("\\.");
        if (split.length != 4) {
            System.out.println("error ip address");
        }
        for (int i = 0; i < split.length; i++) {
            int s = Integer.valueOf(split[i]);
            if (s > 255) {
                System.out.println("error ip address");
            }
            result = (result << 8) | s;
        }
        return result;
    }

    /**
     * 对little-endian字节序进行了转换
     * byte[]转换为long
     * @param b
     * @return ret
     */
    private static BigInteger byteArrayToLong(byte[] b) {
        // long ret = 0;
        BigInteger ret = new BigInteger("0");
        for(int i=0; i<b.length; i++) {
            Long temp = b[i] << (0x8 * i) & (0xFF * (long)(Math.pow(0x100,i)));
            ret = ret.or(new BigInteger(temp.toString()));
        }
        return ret;
    }

    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    private static Long bytesToLong(byte[] b) {
        ByteBuffer buffer = ByteBuffer.allocate(b.length);
        buffer.put(b);
        buffer.flip();
        System.out.println(buffer.getInt());
        return buffer.getLong();
    }

    private static String numToV6(long num) {
        List<Long> addresslist = new ArrayList<>();
        addresslist.add((num>>48)&0xffff);
        addresslist.add((num>>32)&0xffff);
        addresslist.add((num>>16)&0xffff);
        addresslist.add(num&0xffff);
        List<String> hexStr = new ArrayList<>();
        for (Long aLong : addresslist) {
            String s = new BigInteger(aLong.toString()).toString(16).toUpperCase();
            if (s.length() < 4) {
                int max = 4 - s.length();
                for (int i = 0; i < max; i++) {
                    s = "0" + s;
                }
            }
            hexStr.add(s);
        }
        return StringUtils.join(hexStr, ":") + "::";
    }
}
