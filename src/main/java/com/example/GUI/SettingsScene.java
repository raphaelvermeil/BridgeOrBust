package com.example.GUI;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SettingsScene {

    public Scene createSettingsScene(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #A9EDFE;");

        // Add a title or settings controls here
        // This is where you would add your settings options

        // Back button to return to main menu
        Button backButton = new Button("BACK");
        backButton.setLayoutX(250);
        backButton.setLayoutY(300);
        backButton.getStyleClass().add("menu-button");

        // Add hover effect
        addHoverEffect(backButton);

        // Back button action
        backButton.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                GameTitleScreen gameTitleScreen = new GameTitleScreen();
                primaryStage.setScene(gameTitleScreen.createGameTitleScene(primaryStage));

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), primaryStage.getScene().getRoot());
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        });

        root.getChildren().add(backButton);
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("file:style.css");
        return scene;
    }

    /** Adds hover effects to buttons. */
    private void addHoverEffect(Button button) {
        javafx.animation.ScaleTransition stEnter = new javafx.animation.ScaleTransition(Duration.millis(200), button);
        stEnter.setToX(1.3);
        stEnter.setToY(1.2);
        javafx.animation.ScaleTransition stExit = new javafx.animation.ScaleTransition(Duration.millis(200), button);
        stExit.setToX(1.0);
        stExit.setToY(1.0);

        button.setOnMouseEntered(e -> stEnter.playFromStart());
        button.setOnMouseExited(e -> stExit.playFromStart());
    }
}