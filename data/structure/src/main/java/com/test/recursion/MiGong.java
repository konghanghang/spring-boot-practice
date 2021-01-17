package com.test.recursion;

public class MiGong {

    public static void main(String[] args) {
        int[][] map = new int[10][10];
        for (int i = 0; i < 10; i++) {
            map[0][i] = 1;
            map[9][i] = 1;
        }
        for (int i = 0; i < 10; i++) {
            map[i][0] = 1;
            map[i][9] = 1;
        }
        // 设置障碍
        map[3][1] = 1;
        map[3][3] = 1;
        map[4][2] = 1;
        map[5][7] = 1;
        map[6][5] = 1;
        System.out.println("map:");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        setWay(map, 1, 1);

        System.out.println("road:");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }

    }

    /**
     * 找路
     * 按照下-右-上-左走
     * map[8][8]为终点
     * @param map 地图
     * @param x 起点x轴
     * @param y 起点y轴
     * @return
     */
    private static boolean setWay(int[][] map, int x, int y){
        if (map[8][8] == 2){
            return true;
        } else {
            if (map[x][y] == 0) {
                map[x][y] = 2;
                if (setWay(map, x + 1, y)){
                    return true;
                } else if (setWay(map, x, y + 1)){
                    return true;
                } else if (setWay(map, x - 1, y)){
                    return true;
                } else if (setWay(map, x, y-1)) {
                    return true;
                } else {
                    map[x][y] = 3;
                    return false;
                }
            } else {
                return false;
            }
        }
    }

}
