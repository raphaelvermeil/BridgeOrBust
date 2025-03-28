package com.example.GUI;
import com.example.bridgeorbust.physicsSimulation.BridgeSimulation;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameTitleScreen extends Application {
    // Static reference to buttons to maintain consistency
    private static Button playButton;
    private static Button settingsButton;
    private static Button quitButton;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #A9EDFE;"); // Background color

        // Load UI elements
        addHangingLines(root);
        addCircles(root);
        addButtons(root, primaryStage);

        // Set up the scene and stage 600 400
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Swinging Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add("file:style.css");
    }

    /** Adds the hanging lines to the UI. */
    private void addHangingLines(Pane root) {
        Line[] lines = {
                new Line(150, 0, 60, 90), new Line(150, 0, 165, 60),
                new Line(75, 125, 69, 200), new Line(180, 115, 200, 220),
                new Line(57, 240, 63, 315), new Line(206, 250, 167, 310)
        };
        root.getChildren().addAll(lines);
    }

    /** Adds decorative circles to the UI. */
    private void addCircles(Pane root) {
        Circle[] circles = {
                new Circle(25, 8, 30, Color.WHITESMOKE), new Circle(35, 11, 34, Color.WHITESMOKE),
                new Circle(45, 15, 32, Color.WHITESMOKE), new Circle(55, 19, 35, Color.WHITESMOKE),
                new Circle(65, 23, 37, Color.WHITESMOKE), new Circle(75, 22, 36, Color.WHITESMOKE),
                new Circle(85, 26, 34, Color.WHITESMOKE), new Circle(95, 21, 32, Color.WHITESMOKE),
                new Circle(105, 17, 36, Color.WHITESMOKE), new Circle(115, 14, 34, Color.WHITESMOKE),
                new Circle(125, 11, 33, Color.WHITESMOKE), new Circle(135, 7, 27, Color.WHITESMOKE),
                new Circle(145, 4, 22, Color.WHITESMOKE), new Circle(155, 3, 21, Color.WHITESMOKE),
                new Circle(450, 8, 24, Color.FLORALWHITE), new Circle(480, 8, 36, Color.FLORALWHITE),
                new Circle(520, 15, 40, Color.WHITESMOKE), new Circle(560, 12, 26, Color.WHITESMOKE)
        };
        root.getChildren().addAll(circles);
    }

    /** Adds buttons to the UI and assigns functionality. */
    private void addButtons(Pane root, Stage primaryStage) {
        // Only create buttons if they don't exist
        if (playButton == null) {
            playButton = createStyledButton("PLAY", 40, 70, -19);
            settingsButton = createStyledButton("SETTINGS", 30, 190, 8);
            quitButton = createStyledButton("QUIT", 40, 310, -4);

            // Add hover effects
            addHoverEffect(playButton);
            addHoverEffect(settingsButton);
            addHoverEffect(quitButton);

            // Play button action
            playButton.setOnAction(e -> handleButtonClick(primaryStage, root, "play"));

            // Settings button action
            settingsButton.setOnAction(e -> handleButtonClick(primaryStage, root, "settings"));

            // Quit button action
            quitButton.setOnAction(e -> Platform.exit());
        }

        // Always ensure these buttons are in the root
        root.getChildren().removeAll(playButton, settingsButton, quitButton);
        root.getChildren().addAll(playButton, settingsButton, quitButton);
    }

    /** Handles button clicks with smooth transitions. */
    private void handleButtonClick(Stage primaryStage, Pane root, String action) {
        ScaleTransition stClick = new ScaleTransition(Duration.millis(500), root);
        stClick.setToX(1.0);
        stClick.setToY(1.0);
        stClick.setOnFinished(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                if (action.equals("play")) {
                    // Replace with new LevelSelectionScene
                    LevelSelectionScene levelSelectionScene = new LevelSelectionScene();
                    primaryStage.setScene(levelSelectionScene.createLevelSelectionScene(primaryStage));
                } else if (action.equals("settings")) {
                    SettingsScene settingsScene = new SettingsScene();
                    primaryStage.setScene(settingsScene.createSettingsScene(primaryStage));
                }
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), primaryStage.getScene().getRoot());
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        });
        stClick.play();
    }

    /** Creates a styled button. */
    private Button createStyledButton(String text, double x, double y, double rotate) {
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setRotate(rotate);
        button.getStyleClass().add("menu-button");
        return button;
    }

    public Scene createGameTitleScene(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #A9EDFE;");
        addHangingLines(root);
        addCircles(root);
        addButtons(root, primaryStage);
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("file:style.css");
        return scene;
    }

    /** Adds hover effects to buttons. */
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

    public static void main(String[] args) {
        launch(args);
    }
}