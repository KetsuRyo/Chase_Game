package com.example.chessgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DouShouQi extends Application {

    private GameManager gameManager;

    @Override
    public void start(Stage primaryStage) {
        // 显示开始对话框并让玩家输入姓名
        StartDialog.display(primaryStage, (name1, name2) -> {
            // 使用输入的姓名初始化 GameManager
            gameManager = new GameManager(name1, name2);
            gameManager.startNewGame();

            // 创建游戏界面的根布局
            BorderPane root = new BorderPane();
            Label player1 = new Label(name1);
            Label player2 = new Label(name2);

            // 将游戏棋盘添加到布局中
            root.setTop(player1);
            root.setCenter(gameManager.getGameBoard().getGridPane());
            root.setBottom(player2);

            // 创建场景，设置尺寸并添加布局
            Scene scene = new Scene(root, 800, 600);

            // 设置舞台标题
            primaryStage.setTitle("斗兽棋");

            // 将场景设置到舞台上
            primaryStage.setScene(scene);

            // 显示舞台
            primaryStage.show();
        });
    }

    public static void main(String[] args) {
        // 启动JavaFX应用程序
        launch(args);
    }
}

