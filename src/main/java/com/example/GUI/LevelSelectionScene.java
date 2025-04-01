package com.example.GUI;

import com.example.bridgeorbust.physicsSimulation.BridgeSimulation;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelSelectionScene {

    public Scene createLevelSelectionScene(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #A9EDFE;");

        // Add title
        Text title = new Text("SELECT LEVEL");
        title.setLayoutX(230);
        title.setLayoutY(80);
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Level buttons
        Button level1Button = new Button("LEVEL 1");
        level1Button.setLayoutX(250);
        level1Button.setLayoutY(130);
        level1Button.getStyleClass().add("menu-button");

        Button level2Button = new Button("LEVEL 2");
        level2Button.setLayoutX(250);
        level2Button.setLayoutY(200);
        level2Button.getStyleClass().add("menu-button");

        Button level3Button = new Button("LEVEL 3");
        level3Button.setLayoutX(250);
        level3Button.setLayoutY(270);
        level3Button.getStyleClass().add("menu-button");

        // Back button
        Button backButton = new Button("BACK");
        backButton.setLayoutX(50);
        backButton.setLayoutY(340);
        backButton.getStyleClass().add("menu-button");

        // Add hover effects
        addHoverEffect(level1Button);
        addHoverEffect(level2Button);
        addHoverEffect(level3Button);
        addHoverEffect(backButton);

        // Level 1 button action - goes to BridgeSimulation as in original code
        level1Button.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                BridgeSimulation app = new BridgeSimulation();
                app.level=1;
                app.start(primaryStage);
//                primaryStage.close();
            });
            fadeOut.play();
        });

        level2Button.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                BridgeSimulation app = new BridgeSimulation();
                app.level=2;
                app.start(primaryStage);
//                primaryStage.close();
            });
            fadeOut.play();
        });

        // Level 2 and 3 buttons could be configured similarly
        // For now they do nothing

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

        root.getChildren().addAll(title, level1Button, level2Button, level3Button, backButton);

        Scene scene = new Scene(root, 1000, 600);
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