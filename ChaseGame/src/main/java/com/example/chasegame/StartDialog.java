package com.example.chasegame;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StartDialog {

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
            callback.onNamesEntered(name1, name2);

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