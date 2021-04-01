package com.test.graph;

import java.util.*;

/**
 * 临接矩阵方式存储图
 * @author yslao@outlook.com
 * @since 2021/3/31
 */
public class MatrixGraph {

    // 临接矩阵
    private int[][] edges;
    // 图中的顶点
    private List<String> vertexList;
    // 边的个数
    private int numOfEdges;

    private boolean[] visited;

    public MatrixGraph(int n) {
        this.edges = new int[n][n];
        vertexList = new ArrayList<>(n);
        numOfEdges = 0;
    }

    /**
     * 插入节点
     * @param vertex 节点
     */
    public void insertVertex(String vertex){
        vertexList.add(vertex);
    }

    /**
     * 插入边
     * @param v1 在vertexList中的索引
     * @param v2 在vertexList中的索引
     */
    public void insertEdge(int v1, int v2) {
        if (edges[v1][v2] == 1) {
            System.out.println("已存在边");
            return;
        }
        edges[v1][v2] = 1;
        edges[v2][v1] = 1;
        numOfEdges++;
    }

    /**
     * 获取图中节点的个数
     * @return
     */
    public int getNumOfVertex() {
        return vertexList.size();
    }

    /**
     * 获取边的个数
     * @return
     */
    public int getNumOfEdges() {
        return numOfEdges;
    }

    /**
     * 根据索引获取值
     * @return
     */
    public String getValueByIndex(int index) {
        return vertexList.get(index);
    }

    /**
     * 根据索引获取值
     * @return
     */
    public void showGraph() {
        System.out.println(Arrays.deepToString(edges));
    }

    /**
     * 获取vertexList下标为i的节点的相临节点
     * @param i
     * @return
     */
    private int getNeighbor(int i) {
        for (int j = 0; j < vertexList.size(); j++) {
            if (edges[i][j] > 0) {
                return j;
            }
        }
        return -1;
    }

    /**
     * 获取vertexList下标为i的节点的相临节点
     * @param v1 节点v1
     * @param v2 v1的当前临接点下标,从0开始
     * @return
     */
    private int getNextNeighbor(int v1, int v2) {
        for (int j = v2; j < vertexList.size(); j++) {
            if (edges[v1][j] > 0) {
                return j;
            }
        }
        return -1;
    }

    /**
     * 深度优先
     */
    public void dfs() {
        visited = new boolean[vertexList.size()];
        for (int i = 0; i < vertexList.size(); i++) {
            if (!visited[i]) {
                dfs(i);
            }
        }
    }

    /**
     * 从第i个节点开始进行深度优先
     * @param i
     */
    private void dfs(int i) {
        System.out.print(vertexList.get(i) + "=>");
        visited[i] = true;
        for (int j = 0; j < numOfEdges; j++) {
            int w = getNextNeighbor(i, j);
            if (w != -1 && !visited[w]) {
                dfs(w);
            }
        }
    }

    /**
     * 广度优先
     */
    public void bfs() {
        visited = new boolean[vertexList.size()];
        for (int i = 0; i < vertexList.size(); i++) {
            if (!visited[i]) {
                bfs(i);
            }
        }
    }

    /**
     * 从第m个节点开始进行广度优先
     * @param m
     */
    private void bfs(int m) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(m);
        while (!queue.isEmpty()) {
            Integer poll = queue.poll();
            if (!visited[poll]) {
                visited[poll] = true;
                System.out.print(vertexList.get(poll) + "=>");
                for (int i = 0; i < vertexList.size(); i++) {
                    if (getNextNeighbor(poll, i) > 0 && !visited[i]) {
                        queue.offer(i);
                    }
                }
            }
        }
    }
}
