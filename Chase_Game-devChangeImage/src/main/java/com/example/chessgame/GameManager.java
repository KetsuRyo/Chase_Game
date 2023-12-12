package com.example.chessgame;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class GameManager {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private GamePiece piece;
    GameBoard gameBoard = new GameBoard(this);
    public GameManager(String playerName1, String playerName2) {
        this.player1 = new Player(playerName1,this);
        this.player2 = new Player(playerName2,this);
        this.currentPlayer = player1; // 默认从玩家1开始
        startNewGame();

    }
    public GameBoard getGameBoard() {
        return gameBoard;
    }
    // 开始新游戏的方法
    public void startNewGame() {
        // 清空棋盘
        gameBoard.clearBoard(); // 需要在 GameBoard 类中实现这个方法

        // 初始化并放置玩家1的棋子
        initializeAndPlacePieces(player1, true);
        // 初始化并放置玩家2的棋子
        initializeAndPlacePieces(player2, false);
    }

    private void initializeAndPlacePieces(Player player, boolean isPlayerOne) {
        // 定义棋子的名称和力量等级

        String[] names = {"象", "獅", "虎", "豹", "狼", "狗", "貓", "鼠"};
        int[] strengths = {8, 7, 6, 5, 4, 3, 2, 1};


        int[][] positions = isPlayerOne ?
                new int[][]{{2, 6}, {0, 0}, {0, 6}, {2, 2}, {2, 4}, {1, 1}, {1, 5}, {2, 0}} :
                new int[][]{{6, 0}, {8, 6}, {8, 0}, {6, 4}, {6, 2}, {7, 5}, {7, 1}, {6, 6}};

        if (isPlayerOne){
            for (int i = 0; i < names.length; i++) {
                String imagePath = "/animal/" + names[i] + ".png";
                GamePiece piece = new GamePiece(names[i], strengths[i], positions[i][0], positions[i][1], imagePath, player.getGameManager());
                player.addPiece(piece);
                gameBoard.placePiece(piece, positions[i][0], positions[i][1]);
            }
        } else {
            for (int i = 0; i < names.length; i++) {
                String imagePath = "/animal/" + names[i] + "1" + ".jpg";
                GamePiece piece = new GamePiece(names[i], strengths[i], positions[i][0], positions[i][1], imagePath, player.getGameManager());
                player.addPiece(piece);
                gameBoard.placePiece(piece, positions[i][0], positions[i][1]);
            }
        }
    }


    public void showGameOverPopup() {
        // Determine the winner
        String winner = isDenOccupied(player1) ? player1.getName() : player2.getName();

        // Create an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Winner: " + winner);
        alert.setContentText("Click OK to start a new game.");

        // Set an action for the OK button
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                startNewGame();
            }
        });
    }

    public  Player  getCurrentPlayer() {
        return currentPlayer;
    }
    // 检查游戏是否结束的方法
    public boolean isGameOver() {
        // 检查是否有玩家的棋子进入对方的獸穴
        if (isDenOccupied(player1) || isDenOccupied(player2)) {
            return true;
        }

        // 检查是否有玩家已经没有棋子了
        return player1.getPieces().isEmpty() || player2.getPieces().isEmpty();// 如果以上条件都不满足，游戏继续
    }

    // 检查玩家的棋子是否进入了对方的獸穴
    private boolean isDenOccupied(Player player) {
        // 假设獸穴的位置是固定的，例如对于 player1 是 (8, 3)，对于 player2 是 (0, 3)
        int denRow = player == player1 ? 8 : 0;
        int denCol = 3;
        GamePiece piece = gameBoard.getPiece(denRow, denCol);
        return piece != null && player.getPieces().contains(piece);
    }

    // 切换当前玩家的方法
    public void switchPlayer() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    // 其他游戏管理相关的方法
    // ...
}