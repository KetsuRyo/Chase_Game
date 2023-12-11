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
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != null) {
                    // Remove only the ImageView of the piece
                    gridPane.getChildren().remove(board[row][col].getImageView());
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
            // This ensures that the ImageView is added at the correct position
            GridPane.setRowIndex(piece.getImageView(), row);
            GridPane.setColumnIndex(piece.getImageView(), col);
            gridPane.getChildren().add(piece.getImageView());
        }
    }
    public void setupDragHandlers(GamePiece piece) {
        piece.getImageView().setOnMouseReleased(e -> {
            // 转换坐标到 GridPane 的局部坐标系
            double localX = e.getX();
            double localY = e.getY();

            // 计算对应的棋盘格索引
            int newCol = (int) (localX / 50); // 假设每个格子宽度为 50
            int newRow = (int) (localY / 50);

            // 检查是否在棋盘内部
            if (isWithinBoard(newRow, newCol)) {
                // 试图移动棋子
                if (movePiece(piece, newRow, newCol)) {
                    // 如果移动成功, 则更新棋子位置
                    piece.setPosX(newCol); // 更新棋子的X坐标
                    piece.setPosY(newRow); // 更新棋子的Y坐标
                    // 可以在这里添加任何额外的逻辑，如更改当前玩家
                } else {
                    // 如果移动不成功，可能需要将棋子放回原位
                    // 这可以通过重新将棋子的图像放置在它原来的位置来实现
                    gridPane.getChildren().remove(piece.getImageView());
                    gridPane.add(piece.getImageView(), piece.getPosX(), piece.getPosY());
                }
            }
        });
    }
    // 从棋盘上移除棋子
    public void removePiece(int row, int col) {
        if (isWithinBoard(row, col) && board[row][col] != null) {
            gridPane.getChildren().remove(board[row][col].getImageView());
            board[row][col] = null;
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
