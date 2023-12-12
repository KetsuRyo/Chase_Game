package com.example.chessgame;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StartDialog {
    private static GameManager gameManager;

    public static void display(Stage primaryStage, PlayerNamesCallback callback) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL); // 设置为模态窗口
        dialog.setTitle("玩家信息");

        // 创建文本框让玩家输入姓名
        TextField playerName1 = new TextField();
        playerName1.setPromptText("玩家1姓名");

        TextField playerName2 = new TextField();
        playerName2.setPromptText("玩家2姓名");

        // 创建按钮，当点击时开始游戏
        Button startButton = new Button("开始游戏");
        startButton.setOnAction(e -> {
            // 获取玩家输入的姓名
            String name1 = playerName1.getText();
            String name2 = playerName2.getText();

            // 调用回调方法
         //   callback.onNamesEntered(name1, name2);

            gameManager = new GameManager(name1, name2);
            gameManager.startNewGame();

            // 创建游戏界面的根布局
            BorderPane root = new BorderPane();

            // 将游戏棋盘添加到布局中
            root.setCenter(gameManager.getGameBoard().getGridPane());

            // 创建场景，设置尺寸并添加布局
            Scene scene = new Scene(root, 800, 600);

            // 设置舞台标题
            primaryStage.setTitle("斗兽棋");

            // 将场景设置到舞台上
            primaryStage.setScene(scene);

            // 显示舞台
            primaryStage.show();

            // 关闭对话框
            dialog.close();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(playerName1, playerName2, startButton);

        Scene scene = new Scene(layout, 300, 200);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}