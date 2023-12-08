package com.example.chasegame;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class GamePiece {
    private String name;     // 棋子的名称
    private int strength;    // 棋子的力量等级
    private int posX;        // 棋子在棋盘上的X坐标
    private int posY;        // 棋子在棋盘上的Y坐标
    private boolean isAlive; // 棋子是否还在游戏中
    private final ImageView imageView; // 棋子的图形表示

    public GamePiece(String name, int strength, int posX, int posY, String imagePath) {
        this.name = name;
        this.strength = strength;
        this.posX = posX;
        this.posY = posY;
        this.isAlive = true;
        this.imageView = new ImageView(new Image(imagePath));
    }

    // Getter和Setter方法
    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    private boolean isValidMove(int newX, int newY, GameBoard board) {
        // 检查是否为獅或虎
        if (this.name.equals("獅") || this.name.equals("虎")) {
            // 检查是否尝试跳河
            if (isTryingToJumpRiver(newX, newY, board)) {
                return canJumpRiver(newX, newY, board);
            }
        }
        return true; // 对于非獅和虎的棋子，或者不涉及跳河的移动，总是返回true
    }

    // 检查獅或虎是否尝试跳河
    private boolean isTryingToJumpRiver(int newX, int newY, GameBoard board) {
        // 跳河的逻辑，比如检查是否从河的一边移动到另一边
        // ...

        return false; // 示例：默认返回false
    }

    // 检查獅或虎是否可以跳河
    private boolean canJumpRiver(int newX, int newY, GameBoard board) {
        // 检查河中是否有鼠阻挡
        // 可以通过遍历河中的格子来检查
        // ...

        return true; // 示例：默认返回true
    }
    // 棋子移动的方法

    public boolean move(int newX, int newY, GameBoard board) {
        if (!isMoveValid(newX, newY, board)) {
            return false;
        }

        updatePosition(newX, newY);
        return false;
    }

    public boolean isMoveValid(int newX, int newY, GameBoard board) {
        if (!GameUtils.isWithinBoard(newX, newY)) {
            return false; // 移动超出棋盘边界
        }

        // 检查新位置是否是水域，同时考虑棋子类型（只有“鼠”可以进入水域）
        if (board.isRiver(newX, newY) && !this.name.equals("鼠")) {
            return false; // 非“鼠”棋子不能进入水域
        }

        // 检查是否遵守其他移动规则，如“獅”和“虎”跳河的规则
        if (!isValidMove(newX, newY, board)) {
            return false; // 不合法的移动
        }

        return true;
    }
    private void updatePosition(int newX, int newY) {
        this.posX = newX;
        this.posY = newY;
        imageView.setX(newX * 50); // 假设格子大小为50x50
        imageView.setY(newY * 50);
    }
    // 检查棋子是否可以吃掉另一个棋子
    public boolean canDefeat(GamePiece other) {
        // 特殊规则：鼠可以吃象，象不能吃鼠
        if (this.name.equals("鼠") && other.getName().equals("象")) {
            return true;
        }
        if (this.name.equals("象") && other.getName().equals("鼠")) {
            return false;
        }

        // 一般规则：力量等级高的棋子可以吃掉力量等级低的
        return this.strength >= other.getStrength();
    }

    public ImageView getImageView() {
        return imageView;
    }
}
