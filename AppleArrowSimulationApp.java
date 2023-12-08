package com.example.applearrowsimulation;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Random;

public class AppleArrowSimulationApp extends Application {
    private static final double GRAVITY = 9.81;
    private static final double HEIGHT = 100;
    private static final double DISTANCE = 100;
    private static final int MAX_TRIALS = 5;
    private int trial = 1;
    private double appleVelocity;
    private Label resultLabel;
    private Canvas canvas;
    private GraphicsContext gc;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Apple Arrow Simulation");

        Random random = new Random();
        appleVelocity = 10 + (20 - 10) * random.nextDouble();

        Label velocityLabel = new Label("Arrow Velocity (v):");
        TextField velocityField = new TextField();

        Label angleLabel = new Label("Angle (theta) in degrees:");
        TextField angleField = new TextField();

        Button simulateButton = new Button("Simulate");
        simulateButton.setOnAction(e -> simulate(velocityField.getText(), angleField.getText()));

        resultLabel = new Label();

        canvas = new Canvas(200, 100);
        gc = canvas.getGraphicsContext2D();


        VBox layout = new VBox(10,velocityLabel, velocityField, angleLabel, angleField, simulateButton, resultLabel, canvas);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void simulate(String velocityStr, String angleStr) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // 清除画布
        try {
            double arrowVelocity = Double.parseDouble(velocityStr);
            double angle = Double.parseDouble(angleStr);

            // 验证输入
//            10 <= arrowVelocity <= 20
            if (arrowVelocity < 10 || angle < 0 || angle > 90 || arrowVelocity > 20 ) {
                resultLabel.setText("Invalid input. Velocity must be greater than 10 and smaller than 20, angle must be between 0-90 degrees.");
                return;
            }

            angle = Math.toRadians(angle); // 转换为弧度
            boolean hit = simulateHit(appleVelocity, arrowVelocity, angle);

            if (hit) {
                resultLabel.setText("Trial #" + trial + ": Hit!");
            } else {
                resultLabel.setText("Trial #" + trial + ": Miss!");
            }

            if (trial++ >= MAX_TRIALS) {
                trial = 1; // Reset for next set of simulations
            }
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid input. Please enter numerical values.");
        }
    }

    private boolean simulateHit(double appleVelocity, double arrowVelocity, double angle) {
        double timeToGround = Math.sqrt(2 * HEIGHT / GRAVITY);
        double appleDistance = DISTANCE - appleVelocity * timeToGround;
        double appleHeight = HEIGHT - 0.5 * GRAVITY * timeToGround * timeToGround;

        double arrowDistance = arrowVelocity * Math.cos(angle) * timeToGround;
        double arrowHeight = arrowVelocity * Math.sin(angle) * timeToGround - 0.5 * GRAVITY * timeToGround * timeToGround;

        drawTrajectories(appleVelocity, arrowVelocity, angle, timeToGround);

        return Math.abs(appleDistance - arrowDistance) < 1 && Math.abs(appleHeight - arrowHeight) < 1; // 1 meter threshold for hit
    }

    private void drawTrajectories(double appleVelocity, double arrowVelocity, double angle, double timeToGround) {
        // 绘制苹果的轨迹
        double xApple, yApple;

        for (double t = 0; t <= timeToGround; t += 0.05) {
            xApple = appleVelocity * t;
            yApple = HEIGHT - 0.5 * GRAVITY * t * t;
            gc.setFill(Color.RED);
            gc.fillOval(xApple, canvas.getHeight() - yApple, 5, 5); // 苹果的位置
        }
        // 绘制箭的轨迹
        double xArrow, yArrow;
        for (double t = 0; t <= timeToGround; t += 0.05) {
            xArrow = DISTANCE - arrowVelocity * Math.cos(angle) * t;
            yArrow = arrowVelocity * Math.sin(angle) * t - 0.5 * GRAVITY * t * t;
            gc.setFill(Color.BLUE);
            gc.fillOval(xArrow, canvas.getHeight() - yArrow, 5, 5);
             // 箭的位置
        }


    }
}