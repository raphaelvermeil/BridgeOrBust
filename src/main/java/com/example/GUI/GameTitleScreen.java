package com.example.GUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;

public class GameTitleScreen extends Application {
    private Canvas canvas;
    private Button playButton, settingsButton, quitButton;
    private final double WIDTH = 1000;
    private final double HEIGHT = 600;

    @Override
    public void start(Stage stage) {

        MusicManager.startBackgroundMusic();

        // Create a new Pane for the recreated scene
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #5ed3f7");
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add("file:style.css");
        scene.getWidth();
        stage.setMinHeight(150);
        stage.setMinWidth(300);
        // Canvas for lines
        canvas = new Canvas(scene.getWidth(), scene.getHeight());
        root.getChildren().add(canvas);
        Image gifBackground = new Image("file:clouding.gif");
        ImageView gifView = new ImageView(gifBackground);
        gifView.setPreserveRatio(false);
        gifView.setFitWidth(WIDTH);
        gifView.setFitHeight(HEIGHT);
        gifView.setManaged(false);
        gifView.setLayoutY(-157); // Stick to top

// Resize with scene
        scene.widthProperty().addListener((obs, oldVal, newVal) -> gifView.setFitWidth(newVal.doubleValue()));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> gifView.setFitHeight(newVal.doubleValue()));

        canvas = new Canvas(scene.getWidth(), scene.getHeight());
        root.getChildren().add(canvas);


        // Buttons
        playButton = createStyledButton("Play");
        settingsButton = createStyledButton("Settings");
        quitButton = createStyledButton("Quit");

        root.getChildren().addAll(playButton, settingsButton, quitButton,gifView);

        // Button Actions using common handler
        playButton.setOnAction(e -> handleButtonClick(stage, "play"));
        settingsButton.setOnAction(e -> handleButtonClick(stage, "settings"));
        quitButton.setOnAction(e -> Platform.exit());

        // Resize listeners
        scene.widthProperty().addListener((obs, oldVal, newVal) -> draw(scene));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> draw(scene));

        draw(scene);
        stage.setScene(scene);
        stage.setTitle("Bridge or Bust");
        stage.show();
    }

    private void handleButtonClick(Stage stage, String action) {
        Scene currentScene = stage.getScene();

        ScaleTransition stClick = new ScaleTransition(Duration.millis(0), currentScene.getRoot());
        stClick.setToX(1.0);
        stClick.setToY(1.0);
        stClick.setOnFinished(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0), currentScene.getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                if (action.equals("play")) {
                    LevelSelectionScene levelScene = new LevelSelectionScene();
                    stage.setScene(levelScene.createLevelSelectionScene(stage));
                } else if (action.equals("settings")) {
                    SettingsScene settingsScene = new SettingsScene();
                    stage.setScene(settingsScene.createSettingsScene(stage));
                }
                FadeTransition fadeIn = new FadeTransition(Duration.millis(0), stage.getScene().getRoot());
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        });
        stClick.play();
    }

    private void draw(Scene scene) {
        double w = scene.getWidth();
        double h = scene.getHeight();
        canvas.setWidth(w);
        canvas.setHeight(h);

        // Increased the base scale factor by 1.5x to make everything bigger
        double scale = Math.min(w / 800, h / 600) * 1.5;
        // Increased base button size from 140 to 180
        double buttonW = 180 * scale;
        double buttonH = 50 * scale;  // Increased from 40 to 50
        double gap = 50 * scale;      // Increased from 40 to 50

        // Center the elements vertically a bit better
        double playY = h / 3;         // Changed from h/4 to h/3 for better vertical centering
        double settingsY = playY + buttonH + gap;
        double quitY = settingsY + buttonH + gap;
        double centerX = w / 2 - buttonW / 2;

        // Position and resize buttons
        positionAndStyle(playButton, centerX, playY, buttonW, buttonH, scale);
        positionAndStyle(settingsButton, centerX, settingsY, buttonW, buttonH, scale);
        positionAndStyle(quitButton, centerX, quitY, buttonW, buttonH, scale);

        // Draw lines
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);  // Increased line width from 2 to 3

        double centerBtnX = centerX + buttonW / 2;

        // Lines to Play - made the lines spread wider
        gc.strokeLine(w / 3, 0, centerBtnX - 40, playY);  // Changed -20 to -40
        gc.strokeLine(2 * w / 3, 0, centerBtnX + 40, playY);  // Changed +20 to +40

        // Play to Settings - made the lines spread wider
        gc.strokeLine(centerBtnX - 50, playY + buttonH, centerBtnX - 50, settingsY);  // Changed -30 to -50
        gc.strokeLine(centerBtnX + 50, playY + buttonH, centerBtnX + 50, settingsY);  // Changed +30 to +50

        // Settings to Quit - made the lines spread wider
        gc.strokeLine(centerBtnX - 50, settingsY + buttonH, centerBtnX - 50, quitY);  // Changed -30 to -50
        gc.strokeLine(centerBtnX + 50, settingsY + buttonH, centerBtnX + 50, quitY);  // Changed +30 to +50
    }

    private void positionAndStyle(Button button, double x, double y, double width, double height, double scale) {
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        setButtonStyle(button, scale);
    }

    private Button createStyledButton(String text) {
        return new Button(text);
    }

    private void setButtonStyle(Button button, double scale) {
        int fontSize = Math.max(12, (int)(24 * scale));  // Increased from 10/20 to 12/24
        int radius = (int)(85 * scale);
        int padH = (int)(30 * scale);

        String style = "-fx-background-color: white;" +
                "-fx-border-radius: " + radius*8 + "px;" +
                "-fx-background-radius: " + (radius*6) + "px;" +
                "-fx-font-size: " + fontSize + "px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #006699;" +
                "-fx-padding: 12px " + padH + "px;";
        button.setStyle(style);
        buttonEffect(button);
        // In createStyledButton:

    }

    private void addHoverEffect(Button button) {
        ScaleTransition stEnter = new ScaleTransition(Duration.millis(200), button);
        stEnter.setToX(1.3);
        stEnter.setToY(1.2);

        ScaleTransition stExit = new ScaleTransition(Duration.millis(200), button);
        stExit.setToX(1.0);
        stExit.setToY(1.0);

        button.setOnMouseEntered(e -> stEnter.playFromStart());
        button.setOnMouseExited(e -> stExit.playFromStart());
    }

    private void buttonEffect(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.AQUAMARINE);
        glow.setRadius(20);

        button.setOnMouseEntered(e -> {
            button.setEffect(glow);
        });

        button.setOnMouseExited(e -> {
            button.setEffect(null);
        });

        button.setOnMousePressed(e -> {
            String currentStyle = button.getStyle();
            button.setStyle(currentStyle + "-fx-translate-y: 2px;");
        });
    }

    public Scene createGameTitleScene(Stage primaryStage) {
        // Create a new Pane for the recreated scene
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #5ed3f7");
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add("file:style.css");

        Image gifBackground = new Image("file:clouding.gif");
        ImageView gifView = new ImageView(gifBackground);
        gifView.setPreserveRatio(false);
        gifView.setFitWidth(WIDTH);
        gifView.setFitHeight(HEIGHT);
        gifView.setManaged(false);
        gifView.setLayoutY(-160); // Pushes it to the top

        scene.widthProperty().addListener((obs, oldVal, newVal) -> gifView.setFitWidth(newVal.doubleValue()));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> gifView.setFitHeight(newVal.doubleValue()));

        root.getChildren().add(gifView);


        canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        playButton = createStyledButton("Play");
        settingsButton = createStyledButton("Settings");
        quitButton = createStyledButton("Quit");

        addHoverEffect(playButton);
        addHoverEffect(settingsButton);
        addHoverEffect(quitButton);

        root.getChildren().addAll(playButton, settingsButton, quitButton);

        playButton.setOnAction(e -> handleButtonClick(primaryStage, "play"));
        settingsButton.setOnAction(e -> handleButtonClick(primaryStage, "settings"));
        quitButton.setOnAction(e -> Platform.exit());

        scene.widthProperty().addListener((obs, o, n) -> draw(scene));
        scene.heightProperty().addListener((obs, o, n) -> draw(scene));

        draw(scene);
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}