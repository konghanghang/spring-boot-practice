package com.test.graph.dijkstra;

import java.util.Arrays;
import java.util.List;

/**
 * 迪杰斯特拉算法
 * @author yslao@outlook.com
 * @since 2021/4/5
 */
public class Dijkstra {

    // 图的顶点
    private List<String> vertex;

    // 邻接矩阵
    private int[][] matrix;

    private VisitedVertex vv;

    public Dijkstra(List<String> vertex, int[][] matrix) {
        this.vertex = vertex;
        this.matrix = matrix;
    }

    public void showGraph() {
        for (int[] ints : matrix) {
            System.out.println(Arrays.toString(ints));
        }
    }

    /**
     * 算法
     * @param index 出发点的下标
     */
    public void dijkstra(int index) {
        vv = new VisitedVertex(vertex.size(), index);
        updateVV(index);
        // 上边已经处理过了一个顶点，所以下边少处理一个，i才1开始
        for (int i = 1; i < vertex.size(); i++) {
            int indexNoVisited = vv.getIndexNoVisited();
            updateVV(indexNoVisited);
        }
    }

    /**
     * 更新vv对象的preVisited和dis属性
     * @param index
     */
    public void updateVV(int index) {
        int len = 0;
        for (int i = 0; i < matrix[index].length; i++) {
            // 出发顶点到index的距离+index到i到距离
            len = vv.getDis()[index] + matrix[index][i];
            if (vv.getAlready()[i] != 1 && len < vv.getDis()[i]) {
                vv.getPreVisited()[i] = index;
                vv.getDis()[i] =  len;
            }
        }
    }

    public void showDis() {
        for (int i = 0; i < vv.getDis().length; i++) {
            System.out.println(vertex.get(i) + "(" + vv.getDis()[i] + ")");
        }
    }
}
