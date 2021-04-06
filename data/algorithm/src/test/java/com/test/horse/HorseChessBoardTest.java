package com.test.horse;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 马踏棋盘测试
 * @author yslao@outlook.com
 * @since 2021/4/6
 */
class HorseChessBoardTest {

    @Test
    void traversalChessBoard() {
        int n = 8;
        HorseChessBoard board = new HorseChessBoard(n);
        int[][] chessBoard = new int[n][n];
        int row = 2;
        int column = 4;
        board.traversalChessBoard(chessBoard, row - 1, column - 1, 1);
        // 输出棋盘情况
        for (int[] ints : chessBoard) {
            System.out.println(Arrays.toString(ints));
        }
    }
}