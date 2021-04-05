package com.test.graph.dijkstra;

import lombok.Data;

import java.util.Arrays;

/**
 * 已访问顶点信息
 * @author yslao@outlook.com
 * @since 2021/4/5
 */
@Data
public class VisitedVertex {

    // 记录已访问过的顶点，访问过是1，未访问过是0
    private int[] already;

    // 每个下标对应值为前一个顶点下标
    private int[] preVisited;

    // 记录每个顶点距离开始点的距离
    private int[] dis;

    /**
     * 构造方法
     * @param length 节点个数
     * @param index 出发访问的节点下标
     */
    public VisitedVertex(int length, int index) {
        this.already = new int[length];
        this.preVisited = new int[length];
        this.dis = new int[length];
        // 设置距离为最大值
        Arrays.fill(dis, 65535);
        // 设置访问的节点的距离为0
        dis[index] = 0;
        // 设置出发顶点已被访问过
        already[index] = 1;
    }

    /**
     * 找到还没有访问过的距离最短的点
     * @return
     */
    public int getIndexNoVisited() {
        int min = 65535, index = 0;
        for (int i = 0; i < already.length; i++) {
            if (already[i] != 1 && dis[i] < min) {
                min = dis[i];
                index = i;
            }
        }
        already[index] = 1;
        return index;
    }
}
