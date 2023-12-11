package com.example.chessgame;

public class GameUtils {

    // 用于检查给定的坐标是否在棋盘范围内的方法
    public static boolean isWithinBoard(int x, int y) {
        return x >= 0 && x < 7 && y >= 0 && y < 9;
    }

    // 可以添加其他辅助方法，比如判断棋子是否可以移动到某个位置
    // 根据游戏规则进行检查，例如考虑河流、陷阱等
    public static boolean canMoveTo(GamePiece piece, int newX, int newY) {
        // 在这里实现棋子移动的规则


        return true; // 示例
    }

    // 其他可能需要的辅助方法
    // ...
}
