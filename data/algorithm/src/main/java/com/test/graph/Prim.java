package com.test.graph;

import java.util.Arrays;

/**
 * 普里姆算法
 * 求最短路径，利用最小生成树MST
 * @author yslao@outlook.com
 * @since 2021/4/4
 */
public class Prim {

    // 顶点个树
    private int vertexs;
    // 顶点数据
    private String[] data;
    // 顶点权值信息
    private int[][] weight;

    /**
     * 创建图
     * @param data
     * @param weight
     */
    public void createGraph(String[] data, int[][] weight) {
        this.vertexs = data.length;
        this.data = data;
        this.weight = weight;
    }

    /**
     * 打印图
     */
    public void showGraph() {
        for (int[] ints : weight) {
            System.out.println(Arrays.toString(ints));
        }
    }

    /**
     * prim算法
     * @param v 从哪个节点开始访问
     */
    public void prim(int v) {
        int[] visited = new int[data.length];
        visited[v] = 1;
        // 要构成最小生成树，需要的边比顶点少1，所以这里进行vertexs-1次遍历
        int minWeight = 1000;
        int h1 = -1;
        int h2 = -2;
        for (int i = 1; i < vertexs; i++) {
            // 每次从一个顶点开始找，找这个顶点相临的其他顶点
            for (int j = 0; j < vertexs; j++) {
                // 相临的其他顶点
                for (int k = 0; k < vertexs; k++) {
                    if (visited[j] == 1 && visited[k] == 0 && weight[j][k] < minWeight) {
                        // 记录最小值
                        minWeight = weight[j][k];
                        // 记录两点，方便打印边
                        h1 = j;
                        h2 = k;
                    }
                }
            }
            // 一次结束后，就找到了一条最小的边
            System.out.println("边<" + data[h1] + "," + data[h2] + ">, 权值：" + minWeight);
            // 设置h2点已访问过
            visited[h2] = 1;
            // 每次循环结束后，重置minWeight
            minWeight = 1000;
        }
    }
}
