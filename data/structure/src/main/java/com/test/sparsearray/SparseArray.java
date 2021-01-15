package com.test.sparsearray;

public class SparseArray {

    public static void main(String[] args) {
        // 原数组
        int[][] old = new int[11][11];
        old[0][1] = 1;
        old[1][2] = 1;
        old[2][3] = 1;
        System.out.println("原数组如下：");
        for (int[] ints : old) {
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }
            System.out.println();
        }

        // 获取原数组中不是0的数据的个数
        int notZero = 0;
        int row = old.length;
        int cell = old[0].length;
        for (int i = 0; i < row; i++) {
            for (int i1 = 0; i1 < cell; i1++) {
                if (old[i][i1] != 0){
                    notZero++;
                }
            }
        }
        System.out.println("非0的数据有：" + notZero);
        // 转成稀疏数组 多一行存原数组行列，每行固定3列
        int[][] sparseArray = new int[notZero + 1][3];
        sparseArray[0][0] = row;
        sparseArray[0][1] = cell;
        sparseArray[0][2] = notZero;
        int num = 0;
        for (int i = 0; i < row; i++) {
            for (int i1 = 0; i1 < cell; i1++) {
                if (old[i][i1] != 0){
                    num++;
                    sparseArray[num][0] = i;
                    sparseArray[num][1] = i1;
                    sparseArray[num][2] = old[i][i1];
                }
            }
        }
        System.out.println("稀疏数组如下：");
        for (int[] ints : sparseArray) {
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }
            System.out.println();
        }
        // 稀疏数组恢复成原数组
        int[][] oldNew = new int[sparseArray[0][0]][sparseArray[0][1]];
        for (int i = 1; i < sparseArray.length; i++) {
            oldNew[sparseArray[i][0]][sparseArray[i][1]] = sparseArray[i][2];
        }
        System.out.println("恢复后的数组如下：");
        for (int[] ints : oldNew) {
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }
            System.out.println();
        }
    }

}
