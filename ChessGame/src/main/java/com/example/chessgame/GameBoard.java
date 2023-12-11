package com.example.chessgame;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameBoard {
    private GridPane gridPane;
    private GamePiece[][] board; // 棋盘上的棋子，null 表示没有棋子

    public GameBoard() {
        gridPane = new GridPane();
        board = new GamePiece[9][7]; // 根据斗兽棋的棋盘大小调整
        initializeBoard();
    }

    private void initializeBoard() {
        // 初始化棋盘格子
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 7; col++) {
                Rectangle square = new Rectangle(50, 50);
                if (isRiver(row, col)) {
                    square.setFill(Color.BLUE);
                } else if (isTrap(row, col)) {
                    square.setFill(Color.RED);
                } else if (isDen(row, col)) {
                    square.setFill(Color.GREEN);
                } else {
                    square.setFill((row + col) % 2 == 0 ? Color.WHITE : Color.LIGHTGRAY);
                }
                gridPane.add(square, col, row);
            }
        }
    }
    public void clearBoard() {
        // 遍历棋盘数组，移除所有棋子
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != null) {
                    // 如果有棋子，从 GridPane 中移除棋子的图形
                    gridPane.getChildren().clear();
                    // 从数组中移除棋子对象
                    board[row][col] = null;
                }
            }
        }
    }
    // 检查是否是河流
    public boolean isRiver(int row, int col) {
        // 通常河流位于棋盘中间的两个2x3区域
        // 例如，河流可能位于第3行和第4行的第1到第5列
        return (row == 3 || row == 4 || row == 5) && (col == 5 || col == 1 || col == 2 || col == 4);
    }
    public GamePiece getPiece(int row, int col) {
        if (isWithinBoard(row, col)) {
            return board[row][col];
        }
        return null; // 如果位置不在棋盘上，返回 null
    }
    // 检查是否是陷阱
    public boolean isTrap(int row, int col) {
        // 陷阱通常位于獸穴周围
        // 例如，陷阱可能位于底部和顶部獸穴附近的特定位置
        // 假设獸穴位于第0行和第8行的第3列
        return ((row == 0 || row == 8) && (col == 2 || col == 4)) ||
                ((row == 1 || row == 7) && col == 3);
    }

    // 检查是否是獸穴
    public boolean isDen(int row, int col) {
        // 獸穴通常位于棋盘的两端中间位置
        // 例如，獸穴可能位于第0行和第8行的第3列
        return (row == 0 || row == 8) && col == 3;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    // 在特定位置放置棋子
    public void placePiece(GamePiece piece, int row, int col) {
        if (isWithinBoard(row, col)) {
            board[row][col] = piece;
            // 将棋子的图形添加到 GridPane
            gridPane.add(piece.getPieceName(), col, row);
        }
    }

    // 从棋盘上移除棋子
    public void removePiece(int row, int col) {
        if (isWithinBoard(row, col)) {
            board[row][col] = null;
            // 从 GridPane 移除棋子的图形
            // 这需要适当的方法来定位和移除特定的节点
        }
    }

    // 检查给定的坐标是否在棋盘范围内
    public boolean isWithinBoard(int row, int col) {
        return row >= 0 && row < 9 && col >= 0 && col < 7;
    }

    // 根据棋子的当前位置和目标位置来移动棋子
    public boolean movePiece(GamePiece piece, int newRow, int newCol) {
        if (piece != null && isWithinBoard(newRow, newCol)) {
            // 调用 GamePiece 类的 move 方法，传递新的坐标和棋盘
            if (piece.move(newRow, newCol, this)) {
                // 如果移动合法，则更新棋盘上的棋子位置
                removePiece(piece.getPosX(), piece.getPosY());
                placePiece(piece, newRow, newCol);
                return true;
            }
        }
        return false;
    }
}
