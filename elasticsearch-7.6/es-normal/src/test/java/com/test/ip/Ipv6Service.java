package com.test.ip;

import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ipv6Service {

    private String file = "D:\\project\\idea\\spring-boot-demo\\elasticsearch-7.6\\es-normal\\src\\test\\resources\\ipv6wry.db";

    //单一模式实例
    private static Ipv6Service instance = new Ipv6Service();

    private RandomAccessFile randomAccessFile = null;
    // 偏移地址长度
    private int offsetLen;
    // 索引区第一条记录的偏移
    private long firstIndex;
    // 总记录数
    private long indexCount;

    public long getIndexCount() {
        return indexCount;
    }

    private Ipv6Service() {
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            System.out.println("读取文件失败！");
        }
        // 获取偏移地址长度
        byte[] bytes = readBytes(6, 1);
        offsetLen = bytes[0];
        // 索引区第一条记录的偏移
        bytes = readBytes(16, 8);
        BigInteger firstIndexBig = byteArrayToLong(bytes);
        firstIndex = firstIndexBig.longValue();
        // 570063
        System.out.println("索引区第一条记录的偏移:" + firstIndex);
        // 总记录数
        bytes = readBytes(8, 8);
        BigInteger indexCountBig = byteArrayToLong(bytes);
        indexCount = indexCountBig.longValue();
    }

    /**
     * @return 单一实例
     */
    public static Ipv6Service getInstance() {
        return instance;
    }

    private byte[] readBytes(long offset, int num) {
        byte[] ret = new byte[num];
        try {
            randomAccessFile.seek(offset);
            for(int i=0; i != num; i++) {
                ret[i] = randomAccessFile.readByte();
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取文件失败_readBytes");
            return ret;
        }
    }

    /**
     * 对little-endian字节序进行了转换
     * byte[]转换为long
     * @param b
     * @return ret
     */
    private BigInteger byteArrayToLong(byte[] b) {
        // long ret = 0;
        BigInteger ret = new BigInteger("0");
        for(int i=0; i<b.length; i++) {
            Long temp = b[i] << (0x8 * i) & (0xFF * (long)(Math.pow(0x100,i)));
            ret = ret.or(new BigInteger(temp.toString()));
        }
        return ret;
    }

    /**
     * 二分查找
     * @param ip
     * @param l
     * @param r
     * @return
     */
    public long find (BigInteger ip, long l, long r){
        if (r - l <= 1)
            return l;
        long m = (l + r) / 2;
        long o = firstIndex + m * (8 + offsetLen);
        byte[] bytes = readBytes(o,  8);
        long l1 = byteToLong(bytes);
        //BigInteger new_ip = byteArrayToLong(bytes);
        BigInteger new_ip = new BigInteger(l1 + "");
        if (ip.compareTo(new_ip) == -1) {
            return find(ip, l, m);
        } else {
            return find(ip, m, r);
        }
    }

    private long byteToLong(byte[] b) {
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

    public BigInteger ipv6ToNum(String ipStr) {
        // String ipStr = "2400:3200::1";
        int ipCount = 8;
        List<String> parts = new ArrayList<>(Arrays.asList(ipStr.split(":")));
        if (parts.size() < 3) {
            System.out.println("error ip address");
        }
        String last = parts.get(parts.size() - 1);
        if (last.contains(".")) {
            long l = ipv4ToNum(last);
            parts.remove(parts.size() - 1);
            // (l >> 16) & 0xFFFF;
            parts.add(new BigInteger(((l >> 16) & 0xFFFF) + "").toString(16));
            parts.add(new BigInteger((l & 0xFFFF) + "").toString(16));
            System.out.println((l >> 16) & 0xFFFF);
            System.out.println(l & 0xFFFF);
            parts.stream().forEach(a -> System.out.print(a + ":"));
        }
        int emptyIndex = -1;
        for (int i = 0; i < parts.size(); i++) {
            if (StringUtils.isEmpty(parts.get(i))) {
                emptyIndex = i;
            }
        }
        int parts_hi, parts_lo, parts_skipped;
        if (emptyIndex > -1) {
            parts_hi = emptyIndex;
            parts_lo = parts.size() - parts_hi - 1;
            if (StringUtils.isEmpty(parts.get(0))) {
                parts_hi -= 1 ;
                if (parts_hi > 0) {
                    System.out.println("error ip address");
                }
            }
            if (StringUtils.isEmpty(parts.get(parts.size() - 1))) {
                parts_lo -= 1;
                if (parts_lo > 0) {
                    System.out.println("error ip address");
                }
            }
            parts_skipped = ipCount - parts_hi - parts_lo;
            if (parts_skipped < 1) {
                System.out.println("error ip address");
            }
        } else {
            // 完全地址
            if (parts.size() != ipCount) {
                System.out.println("error ip address");
            }
            parts_hi = parts.size();
            parts_lo = 0;
            parts_skipped = 0;
        }
        BigInteger ipNum = new BigInteger("0");
        if (parts_hi > 0) {
            for (int i = 0; i < parts_hi; i++) {
                ipNum = ipNum.shiftLeft(16);
                String part = parts.get(i);
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
            String part = parts.get(parts.size() + i);
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

    public long getIpOff(long findIp) {
        return firstIndex + findIp * (8 + offsetLen);
    }

    public long getIpRecOff(long ip_off) {
        byte[] bytes = readBytes(ip_off + 8, offsetLen);
        BigInteger ip_rec_off_big = byteArrayToLong(bytes);
        return ip_rec_off_big.longValue();
    }

    public String getAddr(long offset) {
        byte[] bytes = readBytes(offset, 1);
        int num = bytes[0];
        if (num == 1) {
            // 重定向模式1
            // [IP][0x01][国家和地区信息的绝对偏移地址]
            // 使用接下来的3字节作为偏移量调用字节取得信息
            bytes = readBytes(offset + 1, offsetLen);
            BigInteger l = byteArrayToLong(bytes);
            return getAddr(l.longValue());
        } else {
            // 重定向模式2 + 正常模式
            // [IP][0x02][信息的绝对偏移][...]
            String cArea = getAreaAddr(offset);
            if (num == 2) {
                offset += 1 + offsetLen;
            } else {
                offset = findEnd(offset) + 1;
            }
            String aArea = getAreaAddr(offset);
            return cArea + "|" + aArea;
        }
    }

    private String getAreaAddr(long offset){
        // 通过给出偏移值，取得区域信息字符串
        byte[] bytes = readBytes(offset, 1);
        int num = bytes[0];
        if (num == 1 || num == 2) {
            bytes = readBytes(offset + 1, offsetLen);
            BigInteger p = byteArrayToLong(bytes);
            return getAreaAddr(p.longValue());
        } else {
            return getString(offset);
        }
    }

    private String getString(long offset) {
        long o2 = findEnd(offset);
        // 有可能只有国家信息没有地区信息，
        byte[] bytes = readBytes(offset, Long.valueOf(o2 - offset).intValue());
        try {
            return new String(bytes, "utf8");
        } catch (UnsupportedEncodingException e) {
            return "未知数据";
        }
    }

    private long findEnd(long offset) {
        int i = Long.valueOf(offset).intValue();
        long l = i + Long.MAX_VALUE;
        for (; i < Long.MAX_VALUE; i++) {
            byte[] bytes = readBytes(i, 1);
            if ("\0".equals(new String(bytes))) {
                break;
            }
        }
        return i;
    }

}
