package com.example.chessgame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.ProgressIndicator;
import javafx.concurrent.Task;

import java.util.Random;

public class AppleArrowSimulationApp extends Application {
    private static final double GRAVITY = 9.82;
    private static final double HEIGHT = 40;
    private static final double DISTANCE = 80;
    private static final int MAX_TRIALS = 5;
    private int trial = 1;
    private double appleVelocity;
    private Label resultLabel;
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean hasFoundSuccessfulShot = false;
    private double successfulArrowVelocity;
    private double successfulAngle;
    private int unsuccessfulAttempts = 0;
    private double[] answer;
    private double[] answer2;
    private Label messageLabel;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Apple Arrow Simulation");
        messageLabel = new Label();
        messageLabel.setVisible(false);
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        Label velocityLabel = new Label("Arrow Velocity (v):");
        TextField velocityField = new TextField();

        Label angleLabel = new Label("Angle (theta) in degrees:");
        TextField angleField = new TextField();

        Button simulateButton = new Button("Simulate");

        simulateButton.setOnAction(e -> simulate(velocityField.getText(), angleField.getText(),appleVelocity,progressIndicator,simulateButton));

        resultLabel = new Label();
        canvas = new Canvas(400, 200);
        gc = canvas.getGraphicsContext2D();

        VBox layout = new VBox(10, velocityLabel, velocityField, angleLabel, angleField, simulateButton, resultLabel, canvas,progressIndicator);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        generateAppleVelocityAsync(progressIndicator, simulateButton);
    }
    private void generateAppleVelocityAsync(ProgressIndicator progressIndicator, Button simulateButton) {
        Task<Double> task = new Task<Double>() {
            @Override
            protected Double call() throws Exception {
                updateMessage("Generating Data...");
                return generateValidAppleVelocity();
            }

            @Override
            protected void updateMessage(String message) {
                super.updateMessage(message);
                Platform.runLater(() -> messageLabel.setText(message));
            }
        };

        progressIndicator.visibleProperty().bind(task.runningProperty());
        messageLabel.visibleProperty().bind(task.runningProperty());
        simulateButton.disableProperty().bind(task.runningProperty()); // 禁用按鈕

        task.setOnSucceeded(e -> {
            appleVelocity = task.getValue();
        });

        new Thread(task).start();
    }
    private void resetAppleVelocityAsync(ProgressIndicator progressIndicator, Button simulateButton) {
        Task<Double> resetTask = new Task<Double>() {
            @Override
            protected Double call() throws Exception {
                return generateValidAppleVelocity();
            }
        };

        progressIndicator.visibleProperty().bind(resetTask.runningProperty());
        simulateButton.disableProperty().bind(resetTask.runningProperty());

        resetTask.setOnSucceeded(e -> {
            appleVelocity = resetTask.getValue();
            progressIndicator.visibleProperty().unbind();
            simulateButton.disableProperty().unbind();
            progressIndicator.setVisible(false);
            simulateButton.setDisable(false);
            // 这里可以进行其他必要的UI更新
        });

        new Thread(resetTask).start();
    }
    private double generateValidAppleVelocity() {
        Random random = new Random();
        double initialAppleVelocity = 10 + (20 - 10) * random.nextDouble(); // 隨機生成初始值
        double[] calculatedValues = calculateInitialVelocityAndAngle(initialAppleVelocity, 20);

        // 首先檢查初始值是否有效
        if (isValidVelocityAndAngle(calculatedValues)) {
            return initialAppleVelocity;
        }

        // 二分搜索：初始值到 20
        double result = binarySearchForVelocity(initialAppleVelocity, 20);
        if (result != -1) {
            return result;
        }

        // 二分搜索：10 到初始值
        result = binarySearchForVelocity(10, initialAppleVelocity);
        if (result != -1) {
            return result;
        }

        // 如果二分搜索都找不到有效值，則返回最初的隨機初始值
        return initialAppleVelocity;
    }

    private double binarySearchForVelocity(double low, double high) {
        double mid;
        double[] calculatedValues;

        while (low <= high) {
            mid = (low + high) / 2;
            calculatedValues = calculateInitialVelocityAndAngle(mid, 20);

            if (isValidVelocityAndAngle(calculatedValues)) {
                high = mid - 0.1;
            } else {
                low = mid + 0.1;
            }
        }

        // 如果找到有效值，返回這個值；否則返回 -1 表示未找到
        if (low > high) {
            return -1;
        }
        return (low + high) / 2;
    }

    private boolean isValidVelocityAndAngle(double[] calculatedValues) {
        return (calculatedValues[0] >= 10 && calculatedValues[0] <= 20) && (calculatedValues[1] >= 0 && calculatedValues[1] <= Math.toRadians(90));
    }

    private void simulate(String velocityStr, String angleStr,  double Velocity,ProgressIndicator progressIndicator, Button simulateButton) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // 清除画布
        try {
            double arrowVelocity = Double.parseDouble(velocityStr);
            double angle = Double.parseDouble(angleStr);
            answer = calculateInitialVelocityAndAngle(Velocity,20);
            answer2 = calculateInitialVelocityAndAngle(Velocity,90);
            System.out.println(answer[0]);
            System.out.println(answer[1]);
            System.out.println(Math.abs(answer[0] - arrowVelocity));
            System.out.println(Math.abs(answer[1] - angle));
            // 验证输入
            // 10 <= arrowVelocity <= 20
            if (arrowVelocity < 10 || angle < 0 || angle > 90 || arrowVelocity > 20) {
                resultLabel.setText("Invalid input. Velocity must be greater than 10 and smaller than 20, angle must be between 0-90 degrees.");
                return;
            }
            double timeToGround = Math.sqrt(2 * HEIGHT / GRAVITY);

            double angleInRadians = Math.toRadians(angle);
            boolean hit = Math.abs(answer[0] - arrowVelocity) < 0.1 && Math.abs(answer[1] - angle) < 0.1;
            drawTrajectories(this.appleVelocity, arrowVelocity, angleInRadians, Math.sqrt(2 * HEIGHT / GRAVITY));
            double finalAppleX = appleVelocity * timeToGround;
            double finalArrowX = DISTANCE - arrowVelocity * Math.cos(angleInRadians) * timeToGround;
            double finalArrowY = arrowVelocity * Math.sin(angleInRadians) * timeToGround - 0.5 * GRAVITY * timeToGround * timeToGround;
            if (hit) {
                resultLabel.setText("Trial #" + trial + ": Hit!");
                if (!hasFoundSuccessfulShot) {
                    successfulArrowVelocity = arrowVelocity;
                    successfulAngle = angle;
                    hasFoundSuccessfulShot = true;
                }
            } else {

                resultLabel.setText(String.format("Trial #%d: Miss! Apple position: (%.2f, 0), Arrow position: (%.2f, %.2f)", trial, finalAppleX, finalArrowX, finalArrowY));

                unsuccessfulAttempts++;

            }
            if (trial++ >= MAX_TRIALS) {
                if (hasFoundSuccessfulShot) {
                    resultLabel.setText("Successful Shot: Arrow Velocity = " + successfulArrowVelocity + ", Angle = " + Math.toDegrees(successfulAngle));
                } else {
                    if(answer[0] != 0 & answer[1] != 0){
                        resultLabel.setText(" You failed. But I can tell you successful Shot: Arrow Velocity = " + answer[0] + ", Angle = " + answer[1]);
                    }else {
                        resultLabel.setText("It's not possible to hit the apple.But I can tell you successful Shot: Arrow Velocity = " + answer2[0] + ", Angle = " + answer2[1]);
                    }
                }
                trial = 1; // Reset for next set of simulations
                hasFoundSuccessfulShot = false;
                unsuccessfulAttempts = 0;
                resetAppleVelocityAsync(progressIndicator, simulateButton);
            }


        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid input. Please enter numerical values.");
        }
    }


    private   boolean simulateHit(double appleVelocity, double arrowVelocity, double arrowAngle) {


        double timeToGround = Math.sqrt(2 * HEIGHT / GRAVITY);
        double timeStep = 0.01; // Time increment for the simulation

        for (double t = 0; t <= timeToGround; t += timeStep) {
            // Calculate the position of the apple
            double appleX = appleVelocity * t;
            double appleY = HEIGHT - 0.5 * GRAVITY * t * t;

            // Calculate the position of the arrow
            double arrowX = DISTANCE-arrowVelocity * Math.cos(Math.toRadians(arrowAngle)) * t;
            double arrowY = arrowVelocity * Math.sin(Math.toRadians(arrowAngle)) * t - 0.5 * GRAVITY * t * t;

            // Check if the arrow is at the same position as the apple
            // I set the error scale to 0.3 , I assume this apple is as big as watermelon
            if (Math.abs(appleX - arrowX) < 0.3 && Math.abs(appleY - arrowY) < 0.3) {
                // The apple is hit by the arrow
                return !(appleY < 0) && !(arrowY < 0); // The apple is not hit by the arrow

            }
        }
        return false; // The apple is not hit by the arrow
    }



    private static double[] calculateInitialVelocityAndAngle(double appleInitialVelocityX, double limitationVelocity) {
        AppleArrowSimulationApp instance = new AppleArrowSimulationApp(); // Create an instance
        double initialVelocity;
        double launchAngle;
        double[] bestSolution = new double[2];

        for (initialVelocity = 10; initialVelocity <= limitationVelocity; initialVelocity += 0.1) {
            for (launchAngle = 0; launchAngle <= 90; launchAngle += 0.1) {
                if (instance.simulateHit(appleInitialVelocityX, initialVelocity, launchAngle)) {
                    bestSolution[0] = initialVelocity;
                    bestSolution[1] = launchAngle;
                    return bestSolution;
                }
            }
        }

        // If no solution found, return [0, 0]
        return new double[]{0, 0};
    }



    private void drawTrajectories(double appleVelocity, double arrowVelocity, double angle, double timeToGround) {
        double scale = 2.0; // This is the scaling factor

        // 绘制苹果的轨迹
        double xApple, yApple;
        for (double t = 0; t <= timeToGround; t += 0.01) {
            xApple = appleVelocity * t * scale;
            yApple = (HEIGHT - 0.5 * GRAVITY * t * t) * scale;
            gc.setStroke(Color.RED);
            gc.setLineWidth(0.1 * scale); // Scale the line width as well
            gc.strokeOval(xApple, canvas.getHeight() - yApple, 1 * scale, 1 * scale); // 苹果的位置
        }

        // 绘制箭的轨迹
        double xArrow, yArrow;
        for (double t = 0; t <= timeToGround; t += 0.01) {
            xArrow = (DISTANCE - arrowVelocity * Math.cos(angle) * t) * scale;
            yArrow = (arrowVelocity * Math.sin(angle) * t - 0.5 * GRAVITY * t * t) * scale;
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(0.1 * scale); // Scale the line width as well
            gc.strokeOval(xArrow, canvas.getHeight() - yArrow, 1 * scale, 1 * scale); // 箭的位置
        }
    }

}
