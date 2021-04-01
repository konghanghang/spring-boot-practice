package com.test.divideandconquer;

/**
 * 汉诺塔
 * @author yslao@outlook.com
 * @since 2021/4/1
 */
public class HanoiTower {

    /**
     * 汉诺塔
     * @param n 几个盘
     * @param a 第一个柱子
     * @param b 第二个柱子
     * @param c 第三个柱子
     */
    public void hanoiTower(int n, char a, char b, char c) {
        if (n == 1) {
            System.out.println("将第" + n + "个盘从" + a + "=>" + c);
        } else {
            // 当n >2时,我们总是看成两个盘,最下面一个盘,和上面所有盘
            // 先把最上面的盘从A->B, 过程中用到C
            hanoiTower(n - 1, a, c, b);
            // 把最下边的盘A->C
            System.out.println("将第" + n + "个盘从" + a + "=>" + c);
            // 把B塔所有的盘移动到C
            hanoiTower(n - 1, b, a, c);
        }
    }

}
