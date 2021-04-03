package com.test.kmp;

/**
 * kmp算法
 * @author yslao@outlook.com
 * @since 2021/4/3
 */
public class KMP {

    public int kmpSearch(String str, String subStr, int[] next) {
        for (int i = 0, j = 0; i < str.length(); i++) {
            // 如果j > 0, 且字符不相等，则调整j的值，根据当前j的值对应到部分匹配表
            if (j > 0 && str.charAt(i) != subStr.charAt(j)) {
                j = next[j - 1];
            }
            // 如果匹配到相同字符，则j++，进行下一个字符匹配
            if (str.charAt(i) == subStr.charAt(j)) {
                j++;
            }
            // j等于子串的长度表示找到了
            if (j == subStr.length()) {
                return i -j + 1;
            }
        }
        return -1;
    }

    /**
     * 返回dest字符串的部分匹配表
     * @param dest
     * @return
     */
    public int[] getNext(String dest) {
        int[] next = new int[dest.length()];
        next[0] = 0;
        for (int i = 1, j  = 0; i < next.length; i++) {
            // 如果j > 0, 且字符不相等，j就一直--，直到找到是相同的字符或者--到0，再从头开始
            while (j > 0 && dest.charAt(i) != dest.charAt(j)) {
                j--;
            }
            // 如果匹配到相同字符，则j++，进行下一个字符匹配
            if (dest.charAt(i) == dest.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
        return next;
    }

}
