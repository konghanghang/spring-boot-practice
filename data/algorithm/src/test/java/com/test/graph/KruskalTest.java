package com.test.graph;

import com.test.graph.kruskal.Kruskal;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 科鲁斯卡尔算法测试
 * @author yslao@outlook.com
 * @since 2021/4/4
 */
class KruskalTest {

    @Test
    void showMatrix() {

        List<String> vertexs = new ArrayList<>();
        vertexs.add("A");
        vertexs.add("B");
        vertexs.add("C");
        vertexs.add("D");
        vertexs.add("E");
        vertexs.add("F");
        vertexs.add("G");
        int[][] matrix = {
                {0, 12, 1000, 1000, 1000, 16, 14},
                {12, 0, 10, 1000, 1000, 7, 1000},
                {1000, 10, 0, 3, 5, 6, 1000},
                {1000, 1000, 3, 0, 4, 1000, 1000},
                {1000, 1000, 5, 4, 0, 2, 8},
                {16, 7, 6, 1000, 2, 0, 9},
                {14, 1000, 1000, 1000, 8, 9, 0},
        };
        Kruskal kruskal = new Kruskal(vertexs, matrix);
        kruskal.showMatrix();
        kruskal.getEdges();
        kruskal.kruskal();
    }
}