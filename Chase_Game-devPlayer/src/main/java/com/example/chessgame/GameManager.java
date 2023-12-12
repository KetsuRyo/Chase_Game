package com.example.chessgame;

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
//        int[][] positions = {{2, 6}, {0, 0}, {0, 6}, {2, 2}, {2, 4}, {1, 1}, {1, 5}, {2, 0}};

        int[][] positions = isPlayerOne ?
                new int[][]{{2, 6}, {0, 0}, {0, 6}, {2, 2}, {2, 4}, {1, 1}, {1, 5}, {2, 0}} :
                new int[][]{{6, 0}, {8, 6}, {8, 0}, {6, 4}, {6, 2}, {7, 5}, {7, 1}, {6, 6}};

        for (int i = 0; i < names.length; i++) {
            String imagePath = "/animal/" + names[i] + ".png";
            // Pass the player's gameManager reference to the GamePiece
            GamePiece piece = new GamePiece(names[i], strengths[i], positions[i][0], positions[i][1], imagePath, player.getGameManager());
            player.addPiece(piece);
            gameBoard.placePiece(piece, positions[i][0], positions[i][1]);
        }
    }

    // 处理棋子移动的方法
//    public void movePiece(GamePiece piece, int newX, int newY) {
//        // 首先检查是否轮到当前玩家操作
//        if (!currentPlayer.getPieces().contains(piece)) {
//            return; // 如果不是当前玩家的棋子，则不能移动
//        }
//
//        if (currentPlayer == player1 && player2.getPieces().contains(piece)){
//            return;
//        }
//
//        if (currentPlayer == player2 && player1.getPieces().contains(piece)){
//            return;
//        }
//
//        // 检查目标位置是否合法
//        if (!GameUtils.isWithinBoard(newX, newY)) {
//            return; // 如果目标位置超出棋盘，移动不合法
//        }
//
//        // 检查是否符合棋子的移动规则
//        if (!piece.isMoveValid(newX, newY, gameBoard)) {
//            return; // 如果移动不符合棋子的规则，移动不合法
//        }
//
//        // 如果目标位置有棋子，检查是否可以吃掉对方棋子
//        GamePiece targetPiece = gameBoard.getPiece(newX, newY);
//        if (targetPiece != null && !piece.canDefeat(targetPiece)) {
//            return; // 如果目标位置有棋子且不能吃掉，移动不合法
//        }
//
//        // 移动棋子并更新棋盘
//        gameBoard.movePiece(piece, newX, newY);
//
//        // 切换到下一个玩家
//        switchPlayer();
//    }

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
        if (player1.getPieces().isEmpty() || player2.getPieces().isEmpty()) {
            return true;
        }

        return false; // 如果以上条件都不满足，游戏继续
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