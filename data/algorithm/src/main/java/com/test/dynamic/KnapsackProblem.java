package com.test.dynamic;

/**
 * 动态规划背包问题
 * @author yslao@outlook.com
 * @since 2021/4/2
 */
public class KnapsackProblem {

    public void knapsack(){
        // 物品重量
        int w[] = {1, 4, 3};
        // 物品价值
        int val[] = {1500, 3000, 2000};
        // 背包重量
        int m = 4;
        // 物品的个数
        int n = val.length;
        // 构造表，行为物品，列为背包重量
        int[][] v = new int[n+1][m+1];

        // 放入商品的情况
        int[][] path = new int[n+1][m+1];

        // 设置背包的第一行和第一列为0，即什么都不存放
        // 设置第一列
        for (int i = 0; i < v.length; i++) {
            v[i][0] = 0;
        }
        // 设置第一行
        for (int i = 0; i < v[0].length; i++) {
            v[0][i] = 0;
        }
        // i从1开始表示不处理第一行，都是0， 行表示的是物品
        for (int i = 1; i < v.length; i++) {
            // j从1开始不处理第一列，都是0， 列表示当前背包的重量
            for (int j = 1; j < v[0].length; j++) {
                // 放入的物品重量大于当前背包的重量，则表明不能放入，拿上一行的当前列数据放入
                if (w[i-1] > j) {
                    v[i][j] = v[i-1][j];
                } else {
                    // 可以放入，则需要求[（放入物品的金额）和（减去放入物品重量后剩余容量可以放的最大值）之和]和[上一行的当前列数据]两者中的最大值放入v[i][j]
                    // v[i][j] = Math.max(v[i-1][j], val[i-1] + v[i-1][j-w[i-1]]);
                    if (v[i-1][j] < val[i-1] + v[i-1][j-w[i-1]]) {
                        v[i][j] = val[i-1] + v[i-1][j-w[i-1]];
                        path[i][j] = 1;
                    } else {
                        v[i][j] = v[i-1][j];
                    }
                }
            }
        }
        // 打印表格
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v[0].length; j++) {
                System.out.print(v[i][j] + "\t");
            }
            System.out.println();
        }

        // 输出放入的商品
        int i = path.length - 1;
        int j = path[0].length - 1;
        // 每一行i和最大j处一定是最大金额
        while (i > 0 && j > 0) {
            if (path[i][j] == 1) {
                System.out.printf("放入第%d个商品\n", i);
                j -= w[i - 1];
            }
            i--;
        }
    }

}
