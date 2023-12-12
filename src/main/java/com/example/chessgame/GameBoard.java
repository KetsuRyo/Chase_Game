package com.example.chessgame;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameBoard {
    private GridPane gridPane;
    private double deltaX;
    private double deltaY;
    private GamePiece[][] board; // 棋盘上的棋子，null 表示没有棋子
    private GameManager gameManager;
    private GamePiece selectedPiece;

    public GameBoard(GameManager gameManager) {
        this.gameManager = gameManager;
        gridPane = new GridPane();
        board = new GamePiece[9][7]; // 根据斗兽棋的棋盘大小调整
        initializeBoard();
        gridPane.setOnMouseReleased(e -> {
            // 处理鼠标释放的逻辑
            handleGlobalMouseRelease(e);
        });
    }

    private void handleGlobalMouseRelease(MouseEvent e) {
        // 确定鼠标释放的位置
        int newCol = (int) Math.floor((e.getSceneX() - deltaX + 25) / 50);
        int newRow = (int) Math.floor((e.getSceneY() - deltaY + 25) / 50);

        // 根据需要处理棋子移动逻辑
        // 您需要一种方式来确定哪个棋子被移动
        // 比如使用一个全局变量来跟踪当前选中的棋子
        if (selectedPiece != null) {
            // movePiece2(selectedPiece, newRow, newCol, gameManager);
            movePiece(selectedPiece, newRow, newCol);
            System.out.println(selectedPiece.getName());
        }
        System.out.println("Mouse Release");
        selectedPiece = null;
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
            ImageView pieceView = (ImageView) piece.getImageView();

            // 如果 pieceView 已经被添加到场景图中，则先从其父节点中移除
            if (pieceView.getParent() != null) {
                ((GridPane) pieceView.getParent()).getChildren().remove(pieceView);
            }

            // 更新棋盘数组
            board[row][col] = piece;

            // 设置事件处理器
            setupPieceEventHandlers(piece, pieceView);

            // 将 pieceView 添加到新位置
            gridPane.add(pieceView, col, row);
        }
    }

    private void setupPieceEventHandlers(GamePiece piece, ImageView pieceView) {
        pieceView.setOnMousePressed(e -> {
            deltaX = e.getSceneX() - pieceView.getLayoutX();
            deltaY = e.getSceneY() - pieceView.getLayoutY();
            selectedPiece = piece; // 更新选中的棋子
            System.out.println("Mouse Pressed");
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
    public void movePiece(GamePiece piece, int newRow, int newCol) {

        if (!isWithinBoard(newRow, newCol)) {
            return; // 目标位置超出棋盘边界
        }

        if (!gameManager.getCurrentPlayer().getPieces().contains(piece)) return;
        System.out.println(newRow + "+" + newCol);
        if (isRiver(newRow, newCol)) {
            // 鼠可以进入河流
            System.out.println("Yes");
            if (piece.getName().equals("鼠")) {
                performMove(piece, newRow, newCol);
            }
    }else {
        System.out.println("Fuck");
            performMove(piece, newRow, newCol);
        }

}


    private void performMove(GamePiece piece, int newRow, int newCol) {
        GamePiece targetPiece = getPiece(newRow, newCol);

        // 如果目标位置有棋子，且当前棋子不能吃掉它，直接返回false
        if (targetPiece != null && !piece.canDefeat(targetPiece)) {
            System.out.println("NONO");
            return; // 目标位置有更强的棋子，不能移动
        }
        if (targetPiece != null && gameManager.getCurrentPlayer().getPieces().contains(targetPiece)) {
            System.out.println("NONONO");
            return; // 目标位置有更强的棋子，不能移动
        }
        // 移除当前棋子所在位置的棋子
        removePiece(piece.getPosX(), piece.getPosY());

        // 如果目标位置有棋子，移除目标位置的棋子（因为它将被吃掉）
        if (targetPiece != null) {
            removePiece(newRow, newCol); // 吃掉对方棋子
        }

        // 在新位置放置棋子
        placePiece(piece, newRow, newCol);
        gameManager.switchPlayer();

    }




}
