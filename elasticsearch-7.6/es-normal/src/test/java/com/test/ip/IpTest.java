package com.test.ip;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
        String ipV6Address = getFormatedAddress("2001:da8:202:10::36");
        bytes = instance.readBytes(firstIndex + 8 * 2, 8);
        System.out.println(instance.byteArrayToLong(bytes));
        BigInteger ipNum = ipv6ToNum("2001:4860:4860::8888");
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
        System.out.println(addr);
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

    private static String getFormatedAddress(String addr) {
        String[] addrSegs = addr.split(":");
        StringBuilder retStrObj = new StringBuilder();
        int segLength = addrSegs.length;

        for (int i = 0; i < segLength; i++) {
            retStrObj.append(ensureLength(addrSegs[i], 4)).append(":");
        }

        return retStrObj.toString();
    }

    /**
     * Ensure length is16.
     *
     * @param value the value
     * @return the string
     */
    private static String ensureLength(String value, int length) {
        int curLen = value.length();
        if (curLen < length) {
            String lPadStr = repeat('0', length - curLen);
            return lPadStr + value;
        } else if (curLen > length) {
            return value.substring(0, length);
        }
        return value;
    }

    /**
     * Repeat.
     *
     * @param ch     the ch
     * @param repeat the repeat
     * @return the string
     */
    public static String repeat(char ch, int repeat) {
        char buf[] = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }

        return new String(buf);
    }

    /**
     * 从IP段获取数字类型表示
     *
     * @param ipSeg 格式化好的IP段，至少是前4段
     * @return the long from ip seg
     */
    private static BigInteger getBigIntegerFromIPSeg(String ipSeg) {
        String noneStyleIP = ipSeg.substring(0, 19).replace(":", "");

        return new BigInteger(noneStyleIP, 16);
    }

    /**
     *
     * 将字符串形式的ip地址转换为BigInteger
     *
     * @param ipInString
     *
     *            字符串形式的ip地址
     *
     * @return 整数形式的ip地址
     */
    public static BigInteger StringToBigInt(String ipInString) {

        ipInString = ipInString.replace(" ", "");

        byte[] bytes;

        if (ipInString.contains(":")) {
            bytes = ipv6ToBytes(ipInString);
        } else {
            bytes = ipv4ToBytes(ipInString);
        }
        return new BigInteger(bytes);
    }

    /**
     *
     * ipv6地址转有符号byte[17]
     *
     * @param ipv6
     *            字符串形式的IP地址
     *
     * @return big integer number
     */
    private static byte[] ipv6ToBytes(String ipv6) {

        byte[] ret = new byte[17];
        ret[0] = 0;
        int ib = 16;

        boolean comFlag = false;// ipv4混合模式标记
        if (ipv6.startsWith(":")) {// 去掉开头的冒号
            ipv6 = ipv6.substring(1);
        }
        String groups[] = ipv6.split(":");

        for (int ig = groups.length - 1; ig > -1; ig--) {// 反向扫描
            if (groups[ig].contains(".")) {
                // 出现ipv4混合模式
                byte[] temp = ipv4ToBytes(groups[ig]);
                ret[ib--] = temp[4];
                ret[ib--] = temp[3];
                ret[ib--] = temp[2];
                ret[ib--] = temp[1];
                comFlag = true;
            } else if ("".equals(groups[ig])) {
                // 出现零长度压缩,计算缺少的组数
                int zlg = 9 - (groups.length + (comFlag ? 1 : 0));
                while (zlg-- > 0) {// 将这些组置0
                    ret[ib--] = 0;
                    ret[ib--] = 0;
                }
            } else {
                int temp = Integer.parseInt(groups[ig], 16);
                ret[ib--] = (byte) temp;
                ret[ib--] = (byte) (temp >> 8);
            }
        }
        return ret;
    }

    /**
     *
     * ipv4地址转有符号byte[5]
     *
     * @param ipv4
     *            字符串的IPV4地址
     *
     * @return big integer number
     */
    private static byte[] ipv4ToBytes(String ipv4) {
        byte[] ret = new byte[5];
        ret[0] = 0;

        // 先找到IP地址字符串中.的位置

        int position1 = ipv4.indexOf(".");
        int position2 = ipv4.indexOf(".", position1 + 1);
        int position3 = ipv4.indexOf(".", position2 + 1);

        // 将每个.之间的字符串转换成整型

        ret[1] = (byte) Integer.parseInt(ipv4.substring(0, position1));
        ret[2] = (byte) Integer.parseInt(ipv4.substring(position1 + 1,
                position2));
        ret[3] = (byte) Integer.parseInt(ipv4.substring(position2 + 1,
                position3));
        ret[4] = (byte) Integer.parseInt(ipv4.substring(position3 + 1));

        return ret;
    }

    public static BigInteger ipv6ToNum(String ipStr) {
        // String ipStr = "2400:3200::1";
        int ipCount = 8;
        String[] parts = ipStr.split(":");
        int emptyIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (StringUtils.isEmpty(parts[i])) {
                emptyIndex = i;
                break;
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
        }
        System.out.println(ipNum);
        return ipNum;
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
}
