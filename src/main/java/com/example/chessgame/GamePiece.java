package com.example.chessgame;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import org.w3c.dom.events.MouseEvent;

import java.io.InputStream;

import static com.example.chessgame.GameUtils.isWithinBoard;

public class GamePiece {
    private final GameManager gameManager;
    private final String name;     // 棋子的名称
    private final int strength;    // 棋子的力量等级
    private int posX;        // 棋子在棋盘上的X坐标
    private int posY;        // 棋子在棋盘上的Y坐标
    private boolean isAlive; // 棋子是否还在游戏中
    private final Text pieceName = new Text(""); // 棋子的图形表示
    private ImageView imageView;
    private double deltaX;
    private double deltaY;


    public GamePiece(String name, int strength, int posX, int posY,  String  imagePath , GameManager gameManager) {
        this.name = name;
        this.strength = strength;
        this.posX = posX;
        this.posY = posY;
        this.isAlive = true;
        this.pieceName.setText(name);
        this.gameManager = gameManager;
        try {
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is == null) {
                System.out.println("gpp");

            }
            Image image = new Image(is);  // 正確的構造器用法
            this.imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);

            // 保持图像的纵横比
            imageView.setPreserveRatio(true);
        } catch (IllegalArgumentException e) {
            System.out.println("gtp");
            // 處理錯誤，例如設置一個預設圖像
        }

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


    // 检查獅或虎是否尝试跳河






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


    public Text getPieceName(){
        return pieceName;
    }

    public Node getImageView() {
        return imageView;
    }


}
