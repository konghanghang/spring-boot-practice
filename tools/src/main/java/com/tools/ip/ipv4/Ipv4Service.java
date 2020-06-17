package com.tools.ip.ipv4;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * * 用来读取QQwry.dat文件，以根据ip获得好友位置，QQwry.dat的格式是
 * 一. 文件头，共8字节
 *   1. 第一个起始IP的绝对偏移， 4字节
 *   2. 最后一个起始IP的绝对偏移， 4字节
 * 二. "结束地址/国家/区域"记录区 四字节ip地址后跟的每一条记录分成两个部分
 *   1. 国家记录
 *   2. 地区记录 但是地区记录是不一定有的。而且国家记录和地区记录都有两种形式
 *     1. 以0结束的字符串
 *     2. 4个字节，一个字节可能为0x1或0x2
 *       a. 为0x1时，表示在绝对偏移后还跟着一个区域的记录，注意是绝对偏移之后，而不是这四个字节之后
 *       b. 为0x2时，表示在绝对偏移后没有区域记录,不管为0x1还是0x2，后三个字节都是实际国家名的文件内绝对偏移
 *      如果是地区记录，0x1和0x2的含义不明，但是如果出现这两个字节，也肯定是跟着3个字节偏移，如果不是 则为0结尾字符串
 * 三. "起始地址/结束地址偏移"记录区
 *   1. 每条记录7字节，按照起始地址从小到大排列
 *     a. 起始IP地址，4字节
 *     b. 结束ip地址的绝对偏移，3字节
 *
 * 注意，这个文件里的ip地址和所有的偏移量均采用little-endian格式，而java是采用 big-endian格式的，要注意转换
 *
 * 参考：www.jianshu.com/p/01d3c19738c2
 *
 */
public class Ipv4Service {

    private String file = "D:\\project\\idea\\spring-boot-demo\\tools\\src\\main\\resources\\qqwry.dat";

    //单一模式实例
    private static Ipv4Service instance = new Ipv4Service();

    private byte[] v4Data;

    private long firstIndexOffset;

    private long lastIndexOffset;

    private long totalIndexCount;

    private static final byte REDIRECT_MODE_1 = 0x01;

    private static final byte REDIRECT_MODE_2 = 0x02;

    static final long IP_RECORD_LENGTH = 7;

    private Ipv4Service() {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            FileInputStream in = new FileInputStream(file)) {
            byte[] b = new byte[1024];
            while(in.read(b) != -1){
                out.write(b);
            }
            v4Data = out.toByteArray();
        } catch (IOException e) {
            System.out.println("读取文件失败！");
        }
        firstIndexOffset = readBytesAsLong(0, 4);
        lastIndexOffset = readBytesAsLong(4, 4);
        totalIndexCount = (lastIndexOffset - firstIndexOffset) / IP_RECORD_LENGTH + 1;
    }

    /**
     * @return 单一实例
     */
    public static Ipv4Service getInstance() {
        return instance;
    }

    public long ipv4ToNum(String ipStr) {
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

    private long readBytesAsLong(long offset, int num) {
        long ret = 0;
        for(int i=0; i < num; i++) {
            byte value = v4Data[(int) (offset + i)];
            int shift = i << 3;
            // 0xff要转long型，防止左移导致数据丢失
            ret |=((long)0xff << shift) & (value << shift);
        }
        return ret;
    }

    public long find (long ip){
        long l = 0;
        long h = totalIndexCount;
        long m = 0;
        while(l <= h){
            m = (l + h) >>> 1 ;
            long indexIP = readBytesAsLong((firstIndexOffset + (m - 1) * IP_RECORD_LENGTH), 4);
            long indexIPNext = readBytesAsLong((firstIndexOffset + m * IP_RECORD_LENGTH), 4);
            if(indexIP <= ip && ip < indexIPNext) {
                return readBytesAsLong((firstIndexOffset + (m - 1) * IP_RECORD_LENGTH + 4), 3);
            } else {
                if(ip > indexIP) {
                    l = m + 1;
                } else if (ip < indexIP) {
                    h = m - 1;
                }
            }
        }
        return -1;
    }

    public String readIPLocation(final int offset) {
        String ret = "";
        try {
            byte redirectMode = v4Data[offset + 4];
            if (redirectMode == REDIRECT_MODE_1) {
                long countryOffset = readBytesAsLong(offset + 5, 3);
                redirectMode = v4Data[(int)countryOffset];
                String r;
                if (redirectMode == REDIRECT_MODE_2) {
                    long l = readBytesAsLong(countryOffset + 1, 3);
                    QQwryString country = readString((int)l);
                    r = country.string;
                    countryOffset = countryOffset + 4;
                } else {
                    final QQwryString country = readString((int)countryOffset);
                    r = country.string;
                    countryOffset += country.byteCountWithEnd;
                }
                ret = r + "|" + readArea((int)countryOffset);
            } else if (redirectMode == REDIRECT_MODE_2) {
                long l = readBytesAsLong(offset + 5, 3);
                String r = readString((int)l).string;
                ret = r + "|" + readArea(offset + 8);
            } else {
                final QQwryString country = readString(offset + 4);
                String r = country.string;
                ret = r + "|" + readArea((int)offset + 4 + country.byteCountWithEnd);
            }

        } catch (Exception e) {
            System.out.println("readIPLocation error");
        }
        return ret;
    }

    private QQwryString readString(int offset) {
        int pos = offset;
        final byte[] b = new byte[128];
        int i;
        for (i = 0, b[i] = v4Data[pos++]; b[i] != 0; b[++i] = v4Data[pos++]);
        try{
            return new QQwryString(new String(b,0,i,"GBK"),i + 1);
        } catch(UnsupportedEncodingException e) {
            return new QQwryString("",0);
        }
    }

    private String readArea(final int offset) {
        byte redirectMode = v4Data[offset];
        if (redirectMode == REDIRECT_MODE_1 || redirectMode == REDIRECT_MODE_2) {
            long areaOffset = readBytesAsLong(offset + 1, 3);
            if (areaOffset == 0) {
                return "";
            } else {
                return readString((int)areaOffset).string;
            }
        } else {
            return readString(offset).string;
        }
    }

    class QQwryString{
        String string;
        int byteCountWithEnd;
        QQwryString(String string, int byteCountWithEnd) {
            this.string = string;
            this.byteCountWithEnd = byteCountWithEnd;
        }
    }

}
