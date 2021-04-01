package com.test.graph;

import org.junit.jupiter.api.Test;

/**
 * 临接矩阵方式存储图
 * @author yslao@outlook.com
 * @since 2021/3/31
 */
class MatrixGraphTest {

    @Test
    void graphTest() {

        String[] arr = {"A", "B", "C", "D", "E"};
        MatrixGraph matrixGraph = new MatrixGraph(arr.length);
        for (String s : arr) {
            matrixGraph.insertVertex(s);
        }
        // A-B A-C B-C B-D B-E
        matrixGraph.insertEdge(0, 1);
        matrixGraph.insertEdge(0, 2);
        matrixGraph.insertEdge(1, 2);
        matrixGraph.insertEdge(1, 3);
        matrixGraph.insertEdge(1, 4);

        matrixGraph.showGraph();

        matrixGraph.dfs();
        System.out.println();
        matrixGraph.bfs();
    }

    @Test
    void graphTest2() {
        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8"};
        MatrixGraph matrixGraph = new MatrixGraph(arr.length);
        for (String s : arr) {
            matrixGraph.insertVertex(s);
        }

        matrixGraph.insertEdge(0, 1);
        matrixGraph.insertEdge(0, 2);
        matrixGraph.insertEdge(1, 3);
        matrixGraph.insertEdge(1, 4);
        matrixGraph.insertEdge(3, 7);
        matrixGraph.insertEdge(4, 7);
        matrixGraph.insertEdge(2, 5);
        matrixGraph.insertEdge(5, 6);

        matrixGraph.showGraph();

        matrixGraph.dfs();
        System.out.println();
        matrixGraph.bfs();
    }
}