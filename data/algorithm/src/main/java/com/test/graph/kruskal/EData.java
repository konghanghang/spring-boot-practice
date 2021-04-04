package com.test.graph.kruskal;

import lombok.Data;

/**
 * 边的顶点权值类
 * @author yslao@outlook.com
 * @since 2021/4/4
 */
@Data
public class EData {

    // 边的起点
    private String start;

    // 边的终点
    private String end;

    // 边的权值
    private int weight;

    public EData(String start, String end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }
}
