package com.test.graph;

import java.util.Arrays;
import java.util.List;

/**
 * 弗洛伊德算法
 * @author yslao@outlook.com
 * @since 2021/4/6
 */
public class Floyd {

    // 图中的顶点
    private List<String> vertex;

    // 距离记录
    private int[][] dis;

    // 前驱节点下标记录
    private int[][] pre;

    public Floyd(List<String> vertex, int[][] matrix) {
        this.vertex = vertex;
        this.dis = matrix;
        this.pre = new int[vertex.size()][vertex.size()];
        // 填充pre
        for (int i = 0; i < pre.length; i++) {
            Arrays.fill(pre[i], i);
        }
    }

    public void show() {
        System.out.println("dis");
        for (int i = 0; i < dis.length; i++) {
            System.out.print(Arrays.toString(dis[i]) + "\t\t\t");
            System.out.print(Arrays.toString(pre[i]));
            System.out.println();
        }
    }

    public void floyd() {
        int len = 0;
        // 从i出发经过k到达j的距离
        // 中间定点
        for (int k = 0; k < vertex.size(); k++) {
            // 出发顶点
            for (int i = 0; i < vertex.size(); i++) {
                // 到达顶点
                for (int j = 0; j < vertex.size(); j++) {
                    len = dis[i][k] + dis[k][j];
                    if (len < dis[i][j]) {
                        dis[i][j] = len;
                        pre[i][j] = pre[k][j];
                    }
                }
            }
        }
    }
}
