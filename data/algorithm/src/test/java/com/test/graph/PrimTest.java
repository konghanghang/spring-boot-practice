package com.test.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 普里姆算法测试
 * @author yslao@outlook.com
 * @since 2021/4/4
 */
class PrimTest {

    @Test
    void showGraph() {
        String[] data = {"A", "B", "C", "D", "E", "F", "G"};
        int[][] weight = {
                {1000, 5, 7, 1000, 1000, 1000, 2},
                {5, 1000, 1000, 9, 1000, 1000, 3},
                {7, 1000, 1000, 1000, 8, 1000, 1000},
                {1000, 9, 1000, 1000, 1000, 4, 1000},
                {1000, 1000, 8, 1000, 1000, 5, 4},
                {1000, 1000, 1000, 4, 5, 1000, 6},
                {2, 3, 1000, 1000, 4, 6, 1000}
        };
        Prim prim = new Prim();
        prim.createGraph(data, weight);
        prim.showGraph();
        prim.prim(0);
    }
}