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
                ((GridPane)pieceView.getParent()).getChildren().remove(pieceView);
            }

            // 更新棋盘数组
            board[row][col] = piece;

            // 设置事件处理器
            setupPieceEventHandlers(piece, pieceView, gameManager);

            // 将 pieceView 添加到新位置
            gridPane.add(pieceView, col, row);
        }
    }
    private void setupPieceEventHandlers(GamePiece piece, ImageView pieceView, GameManager gameManager) {
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
    public boolean movePiece(GamePiece piece, int newRow, int newCol) {
        if (!isWithinBoard(newRow, newCol)) {
            return false; // 目标位置超出棋盘边界
        }

        // 检查目标位置是否是河流
        if (isRiver(newRow, newCol)) {
            // 鼠可以进入河流
            if (piece.getName().equals("鼠")) {
                return performMove(piece, newRow, newCol);
            }

            // 狮子和老虎可以跳河，但河中不能有鼠
            if ((piece.getName().equals("狮") || piece.getName().equals("虎")) && canJumpRiver(newRow, this)) {
                return performMove(piece, newRow, newCol);
            }

            return false; // 其他动物不能进入河流
        } else {
            // 检查是否是单格移动或狮虎跳河
            if (Math.abs(newRow - piece.getPosX()) <= 1 && Math.abs(newCol - piece.getPosY()) <= 1) {
                return performMove(piece, newRow, newCol);
            } else if (piece.getName().equals("狮") || piece.getName().equals("虎")) {
                return canJumpRiver(newRow, this) && performMove(piece, newRow, newCol);
            }

            return false; // 不符合移动规则
        }
    }

    private boolean performMove(GamePiece piece, int newRow, int newCol) {
        GamePiece targetPiece = getPiece(newRow, newCol);
        if (targetPiece == null || piece.canDefeat(targetPiece)) {
            removePiece(piece.getPosX(), piece.getPosY());
            if (targetPiece != null) {
                removePiece(newRow, newCol); // 吃掉对方棋子
            }
            placePiece(piece, newRow, newCol);
            return true;
        }
        return false; // 目标位置有更强的棋子，不能移动
    }



    private boolean canJumpRiver(int newX,  GameBoard board) {
        // 确定跳河尝试是针对哪条河流
        int startCol, endCol;
        if (newX <= 2) { // 如果目标位置在第1列或第2列，检查左边的河流
            startCol = 1;
            endCol = 2;
        } else { // 否则，检查右边的河流
            startCol = 4;
            endCol = 5;
        }

        // 检查指定的河流区域中是否有鼠阻挡
        for (int row = 3; row <= 5; row++) {
            for (int col = startCol; col <= endCol; col++) {
                GamePiece piece = board.getPiece(row, col);
                if (piece != null && piece.getName().equals("鼠")) {
                    return false; // 河中有鼠阻挡，不能跳河
                }
            }
        }
        return true; // 河中没有鼠阻挡，可以跳河
    }
}
