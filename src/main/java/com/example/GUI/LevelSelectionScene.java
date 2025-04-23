package com.example.GUI;

import com.example.bridgeorbust.physicsSimulation.BridgeSimulation;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelSelectionScene {

    public Scene createLevelSelectionScene(Stage primaryStage) {

        StackPane root = new StackPane();

        root.setStyle("-fx-background-color: #5ed3f7ff");

        VBox mainContainer = new VBox(30); // 30px vertical spacing
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40, 20, 40, 20));

        mainContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.8));
        mainContainer.maxWidthProperty().bind(root.widthProperty().multiply(0.9));

        Text title = new Text("SELECT LEVEL");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Broadway", FontWeight.SEMI_BOLD, 43));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.5));
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(4);
        dropShadow.setOffsetY(3);
        title.setEffect(dropShadow);

        Button tutorialButton = createTutorialButton(primaryStage);
        Button level1Button = createPillButton("LEVEL 1");
        Button level2Button = createPillButton("LEVEL 2");
        Button level3Button = createPillButton("LEVEL 3");
        Button backButton = createPillButton("BACK");

        HBox topRow = new HBox(25);
        topRow.setAlignment(Pos.CENTER);
        topRow.getChildren().addAll(tutorialButton, level1Button);

        HBox bottomRow = new HBox(25);
        bottomRow.setAlignment(Pos.CENTER);
        bottomRow.getChildren().addAll(level2Button, level3Button);

        VBox buttonGroups = new VBox(30);
        buttonGroups.setAlignment(Pos.CENTER);
        buttonGroups.getChildren().addAll(topRow, bottomRow);

        level1Button.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                BridgeSimulation app = new BridgeSimulation();
                app.level = 1;
                app.start(primaryStage);
            });
            fadeOut.play();
        });

        level2Button.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                BridgeSimulation app = new BridgeSimulation();
                app.level = 2;
                app.start(primaryStage);
            });
            fadeOut.play();
        });

        level3Button.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                BridgeSimulation app = new BridgeSimulation();
                app.level = 3;
                app.start(primaryStage);
            });
            fadeOut.play();
        });

        backButton.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                GameTitleScreen gameTitleScreen = new GameTitleScreen();
                primaryStage.setScene(gameTitleScreen.createGameTitleScene(primaryStage));

                FadeTransition fadeIn = new FadeTransition(Duration.millis(0), primaryStage.getScene().getRoot());
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        });

        mainContainer.getChildren().addAll(title, buttonGroups, backButton);

        root.getChildren().add(mainContainer);

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add("file:style.css");
        return scene;
    }
    private Button createTutorialButton(Stage primaryStage) {
        Button tutorialButton = createPillButton("TUTORIAL");

        tutorialButton.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(0), tutorialButton.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                TutorialScene tutorialScene = new TutorialScene();
                primaryStage.setScene(tutorialScene.createScene(primaryStage));
            });
            fadeOut.play();
        });

        return tutorialButton;
    }


    private Button createPillButton(String text) {
        Button button = new Button(text);

        button.setStyle(
                "-fx-background-color: white;" + // background
                        "-fx-border-width: 2px;" + // THICK
                        "-fx-border-radius: 1700px;" + // Pill
                        "-fx-background-radius: 1800px;" + // Pill
                        //  "-fx-font-family: 'Broadway';" + //fnt
                        "-fx-font-weight: Bold;  "+
                        "-fx-font-size: 27px;" + // font size
                        "-fx-text-fill: #006699;" + // Txt colour
                        "-fx-padding: 20px 80px;" // padding
        );


        addEnhancedHoverEffect(button);

        return button;
    }

   /**
    @param button The button to enhance
     */
    private void addEnhancedHoverEffect(Button button) {
        String originalStyle = button.getStyle();


        DropShadow glow = new DropShadow();
        glow.setColor(Color.DEEPSKYBLUE);
        glow.setRadius(20);

        javafx.animation.ScaleTransition stEnter = new javafx.animation.ScaleTransition(Duration.millis(200), button);
        stEnter.setToX(1.1);
        stEnter.setToY(1.1);


        javafx.animation.ScaleTransition stExit = new javafx.animation.ScaleTransition(Duration.millis(200), button);
        stExit.setToX(1.0);
        stExit.setToY(1.0);


        button.setOnMouseEntered(e -> {
            button.setEffect(glow);


            stEnter.playFromStart();
        });

        button.setOnMouseExited(e -> {
            button.setEffect(null);
            button.setStyle(originalStyle);
            stExit.playFromStart();
        });

        button.setOnMousePressed(e -> {
            button.setStyle(originalStyle + "-fx-translate-y: 4px; -fx-brightness: 120%;");
        });

            button.setOnMouseReleased(e -> {
            if (button.isHover()) {
                button.setEffect(glow);
            } else {
                button.setEffect(null);
                button.setStyle(originalStyle);
            }
        });
    }
}