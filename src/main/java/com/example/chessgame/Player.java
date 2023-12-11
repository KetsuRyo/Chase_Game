package com.example.chessgame;

import java.util.ArrayList;


public class Player {
    private String name;  // 玩家姓名
    private ArrayList<GamePiece> pieces; // 玩家拥有的棋子
    private GameManager gameManager;
    public Player(String name ,GameManager gameManager) {
        this.name = name;
        this.gameManager = gameManager;
        this.pieces = new ArrayList<>();
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
    public GameManager getGameManager() {
        return gameManager;
    }
    // 可以添加其他与玩家相关的方法，如检查是否还有棋子在游戏中等
    // ...
}
