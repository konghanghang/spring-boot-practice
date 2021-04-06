package com.test.horse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 马踏棋盘(骑士周游)
 * @author yslao@outlook.com
 * @since 2021/4/6
 */
public class HorseChessBoard {

    private final int X;
    private final int Y;
    private boolean[] visited;
    private boolean finished;

    public HorseChessBoard(int n) {
        this.X = n;
        this.Y = n;
        this.visited = new boolean[n * n];
    }

    /**
     * 算法
     * @param board 棋盘
     * @param row 行
     * @param column 列
     * @param step 第几步
     */
    public void traversalChessBoard(int[][] board, int row, int column, int step) {
        // 设置访问步数
        board[row][column] = step;
        // 设置已访问过
        visited[row * X + column] = true;
        List<Point> next = next(new Point(column, row));
        // 使用贪心算法,先遍历下一步少的点
        sort(next);
        while (!next.isEmpty()) {
            Point p = next.remove(0);
            // 判断p是否被访问过
            if (!visited[p.y * X + p.x]) {
                traversalChessBoard(board, p.y, p.x, step + 1);
            }
        }
        if (step < X * Y && !finished) {
            board[row][column] = 0;
            visited[row * X + column] = false;
        } else {
            finished = true;
        }
    }

    /**
     * 根据当前点位置,确定马下一步可以走的点,最多8个
     * @param current 当前节点
     * @return
     */
    public List<Point> next(Point current) {
        List<Point> ps = new ArrayList<>();

        Point p1 = new Point();
        if ((p1.x = current.x - 2) >= 0 && (p1.y = current.y - 1) >= 0) {
            ps.add(new Point(p1));
        }
        if ((p1.x = current.x - 1) >= 0 && (p1.y = current.y - 2) >= 0) {
            ps.add(new Point(p1));
        }
        if ((p1.x = current.x + 1) < X && (p1.y = current.y - 2) >= 0) {
            ps.add(new Point(p1));
        }
        if ((p1.x = current.x + 2) < X && (p1.y = current.y - 1) >= 0) {
            ps.add(new Point(p1));
        }
        if ((p1.x = current.x + 2) < X && (p1.y = current.y + 1) < Y) {
            ps.add(new Point(p1));
        }
        if ((p1.x = current.x + 1) < X && (p1.y = current.y + 2) < Y) {
            ps.add(new Point(p1));
        }
        if ((p1.x = current.x - 1) >= 0 && (p1.y = current.y + 1) < Y) {
            ps.add(new Point(p1));
        }
        if ((p1.x = current.x - 2) >= 0 && (p1.y = current.y + 1) < Y) {
            ps.add(new Point(p1));
        }
        return ps;
    }

    /**
     * 进行下一步排序
     * @param ps
     */
    private void sort(List<Point> ps) {
        ps.sort((o1, o2) -> {
            int sizeP1 = next(o1).size();
            int sizeP2 = next(o2).size();
            return sizeP1 - sizeP2;
        });
    }
}
