package com.example.GUI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class TutorialScene {
    private Stage primaryStage;
    private BorderPane root;
    private ImageView gifView;
    private int currentGifIndex = 0;

    public Scene createScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
        root = new BorderPane();
        root.setStyle("-fx-background-color: #112233;");

        // Create initial scene
        Scene scene = new Scene(root, 1000, 600);

        // Create gifView once
        gifView = new ImageView();
        gifView.setPreserveRatio(false);
        gifView.setFitWidth(scene.getWidth());
        gifView.setFitHeight(scene.getHeight() * 0.8);

        // Listen for window resizing once
        scene.widthProperty().addListener((obs, oldVal, newVal) ->
                gifView.setFitWidth(newVal.doubleValue()));

        scene.heightProperty().addListener((obs, oldVal, newVal) ->
                gifView.setFitHeight(newVal.doubleValue() * 0.8));

        // Put gifView inside a center pane
        StackPane centerPane = new StackPane(gifView);
        centerPane.setAlignment(Pos.CENTER);
        root.setCenter(centerPane);

        // Now load the first GIF
        loadGif(scene, 0);

        return scene;
    }

    private void loadGif(Scene scene, int index) {
        currentGifIndex = index;

        // Choose which GIF to load based on index
        String gifName;
        switch (index) {
            case 0: gifName = "GBOB1"; break;
            case 1: gifName = "GBOB2"; break;
            case 2: gifName = "GBOB3"; break;
            case 3: gifName = "GBOB4"; break;
            default: gifName = "GBOB1"; break;
        }

        try {
            // Load and set new image (reuse same gifView!)
            gifView.setImage(new Image("file:" + gifName + ".gif"));

            // Rebuild bottom buttons
            createNavigationButtons(scene, index);

        } catch (Exception e) {
            System.err.println("Error loading GIF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createNavigationButtons(Scene scene, int index) {
        HBox buttonsBox = new HBox();
        buttonsBox.setPadding(new javafx.geometry.Insets(15));
        buttonsBox.setAlignment(Pos.CENTER);

        // Left side - Previous button
        HBox leftBox = new HBox(10);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        // Add previous button to all slides except the first one
        if (index > 0) {
            Button previousButton = createStyledButton("Previous");
            previousButton.setOnAction(e -> loadGif(scene, index - 1));
            leftBox.getChildren().add(previousButton);
        }

        // Flexible space in the middle
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right side - Next/Skip/Finish buttons
        HBox rightBox = new HBox(10);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        // Add Next button to all slides except the last one
        if (index < 3) {
            Button nextButton = createStyledButton("Next");
            nextButton.setOnAction(e -> loadGif(scene, index + 1));
            rightBox.getChildren().add(nextButton);
        }

        // Add Finish button only to the last slide
        if (index == 3) {
            Button finishButton = createStyledButton("Finish Tutorial");
            finishButton.setOnAction(e -> returnToTitleScreen());
            rightBox.getChildren().add(finishButton);
        }

        // Add Skip button to all slides except the last one
        if (index < 3) {
            Button skipButton = createStyledButton("Skip Tutorial");
            skipButton.setOnAction(e -> returnToTitleScreen());
            rightBox.getChildren().add(skipButton);
        }

        // Add all components to the main box
        buttonsBox.getChildren().addAll(leftBox, spacer, rightBox);

        root.setBottom(buttonsBox);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);

        // Apply consistent styling
        button.setStyle("-fx-background-color: #2C3E50; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 20px; " +
                "-fx-background-radius: 5px;");

        // Add hover effect
        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #34495E; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-radius: 5px;")
        );

        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: #2C3E50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-radius: 5px;")
        );

        return button;
    }

    private void returnToTitleScreen() {
        GameTitleScreen gameTitleScreen = new GameTitleScreen();
        Scene scene = gameTitleScreen.createGameTitleScene(primaryStage);
        primaryStage.setScene(scene);

    }
}