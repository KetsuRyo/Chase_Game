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
    private GameManager gameManager;
    private String name;     // 棋子的名称
    private int strength;    // 棋子的力量等级
    private int posX;        // 棋子在棋盘上的X坐标
    private int posY;        // 棋子在棋盘上的Y坐标
    private boolean isAlive; // 棋子是否还在游戏中
    private Text pieceName = new Text(""); // 棋子的图形表示
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
        setupMouseHandlers();
    }
    private void setupMouseHandlers() {
//        Drag pieces
        imageView.setOnDragDetected(e -> {
            Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(name);
            db.setContent(content);
        });

        imageView.setOnMousePressed(e -> {
            deltaX = e.getSceneX() - imageView.getLayoutX();
            deltaY = e.getSceneY() - imageView.getLayoutY();
        });

        imageView.setOnMouseDragged(e -> {
            imageView.setLayoutX(e.getSceneX() - deltaX);
            imageView.setLayoutY(e.getSceneY() - deltaY);
        });

        imageView.setOnMouseReleased(e -> {
            int newCol = (int) Math.floor((imageView.getLayoutX() + 25) / 50);
            int newRow = (int) Math.floor((imageView.getLayoutY() + 25) / 50);
            imageView.setLayoutX(newCol * 50);
            imageView.setLayoutY(newRow * 50);
            gameManager.movePiece(this, newRow, newCol);
        });
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
        int divideX = Math.abs(newX - this.posX) / 2;
        int divideY = Math.abs(newY - this.posY) / 2 ;
        if (board.isRiver(divideX, divideY)) return true;
        else return false;

         // 示例：默认返回false
    }

    // 检查獅或虎是否可以跳河
    private boolean canJumpRiver(int newX, int newY, GameBoard board) {
        // 检查河中是否有鼠阻挡
        // 可以通过遍历河中的格子来检查
        // ...
        for (int row = 3; row < 6; row ++){
            for (int col = 1; col < 3; col++){
                if (board.getPiece(row, col).name == "鼠") {
                    return false;
                }
            }
        }

        for (int row = 3; row < 6; row ++){
            for (int col = 4; col < 6; col++){
                if (board.getPiece(row, col).name == "鼠") {
                    return false;
                }
            }
        }
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
        if (!isWithinBoard(newX, newY)) {
            return false; // 移动超出棋盘边界
        }

        //        检查是否前后左右移动
        if (posX != newX && posY != newY){
            return false;
        }

        // 检查新位置是否是水域，同时考虑棋子类型（只有“鼠”可以进入水域）
        if (board.isRiver(newX, newY) && !this.name.equals("鼠")) {
            return false; // 非“鼠”棋子不能进入水域
        }

        // 检查是否遵守其他移动规则，如“獅”和“虎”跳河的规则
        if (!isValidMove(newX, newY, board)) {
            return false; // 不合法的移动
        }

//        检查跳河的是否是狮虎
        if (isTryingToJumpRiver(newX, newY, board) && !(this.name.equals("虎")) && (this.name.equals("狮"))){
            return false;
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


    public Text getPieceName(){
        return pieceName;
    }

    public Node getImageView() {
        return imageView;
    }


}
