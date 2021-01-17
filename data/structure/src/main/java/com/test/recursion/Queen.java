package com.test.recursion;

/**
 * 八皇后问题
 */
public class Queen {

    private static int max = 8;
    private static int[] array = new int[max];// 存放皇后

    public static void main(String[] args) {
            Queen queen = new Queen();
            queen.check(0);
    }

    private void check(int n){
        if (n == max){
            print();
            return;
        }
        for (int i = 0; i < max; i++) {
            // 把第n个皇后放在i列
            array[n] = i;
            // 判断当放置第n个皇后到i列时，是否冲突
            if (judge(n)){
                // 接着放n+1个皇后,即开始递归
                check(n + 1);
            }
            // 如果冲突，就继续执行 array[n] = i; 即将第n个皇后，放置在本行的后移的一个位置
        }
    }

    /**
     * 判断是否有冲突
     * @param n 代表第几个皇后 从0开始
     * @return
     */
    public static boolean judge(int n){
        for (int i = 0; i < n; i++) {
            // 说明
            // 1. array[i] == array[n] 表示判断 第n个皇后是否和前面的n-1个皇后在同一列
            // 2. Math.abs(n-i) == Math.abs(array[n] - array[i]) 表示判断第n个皇后是否和第i皇后是否在同一斜线
            // n = 1 放置第 2列 1 n = 1 array[1] = 1
            // Math.abs(1-0) == 1 Math.abs(array[n] - array[i]) = Math.abs(1-0) = 1
            // 3. 判断是否在同一行, 没有必要，n 每次都在递增
            if (array[i] == array[n] || Math.abs(n - i) == Math.abs(array[n] - array[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 打印可行解
     */
    public static void print(){
        for (int i : array) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
