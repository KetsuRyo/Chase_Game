package com.example.chasegame;

import java.util.ArrayList;

public class Player {
    private String name;  // 玩家姓名
    private ArrayList<GamePiece> pieces; // 玩家拥有的棋子

    public Player(String name) {
        this.name = name;
        this.pieces = new ArrayList<>();
        initializePieces();
    }

    private void initializePieces() {
        // 棋子的名称和力量等级，以及初始位置
        // 假设这些是根据游戏规则预设的
        String[] names = {"象", "獅", "虎", "豹", "狼", "狗", "貓", "鼠"};
        int[] strengths = {8, 7, 6, 5, 4, 3, 2, 1};
        int[][] positions = {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}};

        for (int i = 0; i < names.length; i++) {
            GamePiece piece = new GamePiece(names[i], strengths[i], positions[i][0], positions[i][1], "path/to/image.png");
            addPiece(piece);
        }
    }

    // Getter 和 Setter 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<GamePiece> getPieces() {
        return pieces;
    }

    public void addPiece(GamePiece piece) {
        if (piece != null) {
            pieces.add(piece);
        }
    }

    // 可以添加其他与玩家相关的方法，如检查是否还有棋子在游戏中等
    // ...
}
