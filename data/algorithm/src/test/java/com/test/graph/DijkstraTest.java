package com.test.graph;

import com.test.graph.dijkstra.Dijkstra;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 迪杰斯特拉算法测试
 * @author yslao@outlook.com
 * @since 2021/4/5
 */
class DijkstraTest {

    @Test
    void dijkstra() {
        List<String> vertex = new ArrayList<>();
        vertex.add("A");
        vertex.add("B");
        vertex.add("C");
        vertex.add("D");
        vertex.add("E");
        vertex.add("F");
        vertex.add("G");
        int[][] matrix = new int[vertex.size()][vertex.size()];
        final int N = 65535;
        matrix[0] = new int[]{N, 5, 7, N, N, N, 2};
        matrix[1] = new int[]{5, N, N, 9, N, N, 3};
        matrix[2] = new int[]{7, N, N, N, 8, N, N};
        matrix[3] = new int[]{N, 9, N, N, N, 4, N};
        matrix[4] = new int[]{N, N, 8, N, N, 5, 4};
        matrix[5] = new int[]{N, N, N, 4, 5, N, 6};
        matrix[6] = new int[]{2, 3, N, N, 4, 6, N};

        Dijkstra dijkstra = new Dijkstra(vertex, matrix);
        dijkstra.showGraph();
        dijkstra.dijkstra(6);
        dijkstra.showDis();
    }

}