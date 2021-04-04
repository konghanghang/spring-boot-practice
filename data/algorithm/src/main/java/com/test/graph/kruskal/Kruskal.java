package com.test.graph.kruskal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 科鲁斯卡尔算法
 * @author yslao@outlook.com
 * @since 2021/4/4
 */
public class Kruskal {

    // 顶点
    private List<String> vertexs;

    // 邻接矩阵
    private int[][] matrix;
    
    // 边对象
    private List<EData> edges;

    public Kruskal(List<String> vertexs, int[][] matrix) {
        this.vertexs = vertexs;
        this.matrix = matrix;
    }

    public void showMatrix() {
        for (int[] ints : matrix) {
            System.out.println(Arrays.toString(ints));
        }
    }

    /**
     * 构造边对象
     */
    public void getEdges() {
        edges = new ArrayList<>();
        for (int i = 0; i < vertexs.size(); i++) {
            // 无向图的邻接矩阵是一个对称矩阵，所以这里只存储上三角矩阵的数据
            for (int j = i + 1; j < vertexs.size(); j++) {
                if (matrix[i][j] != 1000) {
                    EData eData = new EData(vertexs.get(i), vertexs.get(j), matrix[i][j]);
                    edges.add(eData);
                }
            }
        }
        edges = edges.stream().sorted(Comparator.comparingInt(EData::getWeight)).collect(Collectors.toList());
        System.out.println("构造边对象完成");
    }

    /**
     * 获取下标为i的顶点的终点，用于判断两个顶点的终点是否相同
     * @param ends 记录各个顶点的终点是哪个，在算法实现过程中动态生成
     * @param i 顶点对应的下标
     * @return
     */
    private int getEnd(int[] ends, int i) {
        while (ends[i] != 0) {
            i = ends[i];
        }
        return i;
    }

    /**
     * kruskal算法
     */
    public void kruskal() {
        // result存放最终选择的边
        List<EData> result = new ArrayList<>();
        int[] ends = new int[vertexs.size()];
        for (int i = 0; i < edges.size(); i++) {
            EData eData = edges.get(i);
            int m = getEnd(ends, vertexs.indexOf(eData.getStart()));
            int n = getEnd(ends, vertexs.indexOf(eData.getEnd()));
            if (m != n) {
                // 设置m的终点为n
                ends[m] = n;
                result.add(eData);
            }
        }
        System.out.println("最后选择的边：");
        for (EData eData : result) {
            System.out.println(eData);
        }
    }
}
