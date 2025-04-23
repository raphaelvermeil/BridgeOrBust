package com.example.GUI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TutorialScene {
    private Stage primaryStage;
    private BorderPane root;
    private int currentGifIndex = 0;

    public Scene createScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
        root = new BorderPane();
        root.setStyle("-fx-background-color: #112233;");

        // Create the initial scene first
        Scene scene = new Scene(root, 1000, 600);

        // Now load the first GIF
        loadGif(scene, 0);

        return scene;
    }

    private void loadGif(Scene scene, int index) {
        currentGifIndex = index;
        root.setCenter(null);
        root.setBottom(null);

        // Choose which GIF to load based on index
        String gifName;
        switch (index) {
            case 0: gifName = "a"; break;
            case 1: gifName = "b"; break;
            case 2: gifName = "c"; break;
            case 3: gifName = "d"; break;
            default: gifName = "a"; break;
        }

        try {
            // Create GIF view
            Image gifImage = new Image("file:" + gifName + ".gif");
            ImageView gifView = new ImageView(gifImage);
            gifView.setPreserveRatio(false);
            gifView.setFitWidth(scene.getWidth());
            gifView.setFitHeight(scene.getHeight() * 0.8); // 80% of height

            // Setup responsive sizing
            scene.widthProperty().addListener((obs, oldVal, newVal) ->
                    gifView.setFitWidth(newVal.doubleValue()));

            scene.heightProperty().addListener((obs, oldVal, newVal) ->
                    gifView.setFitHeight(newVal.doubleValue() * 0.8));

            // Center the GIF
            StackPane centerPane = new StackPane(gifView);
            centerPane.setAlignment(Pos.CENTER);
            root.setCenter(centerPane);

            // Create navigation buttons based on which GIF we're showing
            createNavigationButtons(scene, index);

        } catch (Exception e) {
            System.err.println("Error loading GIF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createNavigationButtons(Scene scene, int index) {
        HBox buttonsBox = new HBox(10);
        buttonsBox.setPadding(new javafx.geometry.Insets(15));

        if (index == 0) {
            // First GIF: only Next and Skip buttons on right
            buttonsBox.setAlignment(Pos.CENTER_RIGHT);

            Button nextButton = new Button("Next");
            nextButton.setOnAction(e -> loadGif(scene, 1));

            Button skipButton = new Button("Skip Tutorial");
            skipButton.setOnAction(e -> returnToTitleScreen());

            buttonsBox.getChildren().addAll(nextButton, skipButton);

        } else if (index == 3) {
            // Last GIF: only Finish button on right
            buttonsBox.setAlignment(Pos.CENTER_RIGHT);

            Button finishButton = new Button("Finish Tutorial");
            finishButton.setOnAction(e -> returnToTitleScreen());

            buttonsBox.getChildren().add(finishButton);

        } else {
            // Middle GIFs: Previous on left, Next and Skip on right
            buttonsBox.setPrefWidth(scene.getWidth());

            HBox leftBox = new HBox();
            leftBox.setAlignment(Pos.CENTER_LEFT);

            Button previousButton = new Button("Previous");
            previousButton.setOnAction(e -> loadGif(scene, index - 1));
            leftBox.getChildren().add(previousButton);

            HBox rightBox = new HBox(0);
            rightBox.setAlignment(Pos.CENTER_RIGHT);

            Button nextButton = new Button("Next");
            nextButton.setOnAction(e -> loadGif(scene, index + 1));

            Button skipButton = new Button("Skip Tutorial");
            skipButton.setOnAction(e -> returnToTitleScreen());

            rightBox.getChildren().addAll(nextButton, skipButton);

            buttonsBox.getChildren().addAll(leftBox, rightBox);
            buttonsBox.setSpacing(scene.getWidth() - 300); // Adjust spacing to push buttons to edges
        }

        root.setBottom(buttonsBox);
    }

    private void returnToTitleScreen() {
        GameTitleScreen gameTitleScreen = new GameTitleScreen();
        Scene scene = gameTitleScreen.createGameTitleScene(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(GameSettings.isFullscreen());
    }
}