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
    //c parfait encore parfait encore
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #A9EDFE;"); // Background color

        // Load UI elements
        addHangingLines(root);
        addCircles(root);
        //add for images.
        // addImages(root);
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
                new Circle(85, 26, 34, Color.WHITESMOKE), new Circle(95, 21, 32, Color.WHITESMOKE ),
                new Circle(105, 17, 36, Color.WHITESMOKE), new Circle(115, 14, 34, Color.WHITESMOKE),
                new Circle(125, 11, 33, Color.WHITESMOKE), new Circle(135, 7, 27, Color.WHITESMOKE),
                new Circle(145, 4, 22, Color.WHITESMOKE), new Circle(155, 3, 21, Color.WHITESMOKE),
                new Circle(450, 8, 24, Color.FLORALWHITE), new Circle(480, 8, 36, Color.FLORALWHITE),
                new Circle(520, 15, 40, Color.WHITESMOKE), new Circle(560, 12, 26, Color.WHITESMOKE)
        };
        root.getChildren().addAll(circles);
    }

    /** Adds images to the UI. */
    /*
    private void addImages(Pane root) {
        //logo image
    ImageView logo = new ImageView(new Image("file:logo.png"));
        logo.setX(250);
        logo.setY(95);
        logo.setFitWidth(285);
        logo.setFitHeight(330);

        ImageView bridge = new ImageView(new Image("file:bridge.PNG"));
        bridge.setX(175);
        bridge.setY(95);
        bridge.setFitWidth(485);
        bridge.setFitHeight(320);

        //add logo if needed
        root.getChildren().addAll(bridge);
    }

     */

    /** Adds buttons to the UI and assigns functionality. */
    private void addButtons(Pane root, Stage primaryStage) {
        Button playButton = createStyledButton("PLAY", 40, 70, -19);
        Button settingsButton = createStyledButton("SETTINGS", 30, 190, 8);
        Button quitButton = createStyledButton("QUIT", 40, 310, -4);
        //testing effect 1
        ScaleTransition stEnter = new ScaleTransition(Duration.millis(200), playButton);
        stEnter.setToX(1.3);
        stEnter.setToY(1.2);

        ScaleTransition stExit = new ScaleTransition(Duration.millis(200), playButton);
        stExit.setToX(1.0);
        stExit.setToY(1.0);

        playButton.setOnMouseEntered(e -> stEnter.playFromStart());
        playButton.setOnMouseExited(e -> stExit.playFromStart());

        // Add hover effects
        addHoverEffect(playButton);
        addHoverEffect(settingsButton);
        addHoverEffect(quitButton);

        // Quit button action
        quitButton.setOnAction(e -> Platform.exit());
        addQuitButtonEffect(quitButton);

        // Play button action
        playButton.setOnMouseClicked(e -> {
            BridgeSimulation app = new BridgeSimulation();
            app.start(new Stage());
            primaryStage.close();
        });

        root.getChildren().addAll(playButton, settingsButton, quitButton);
    }


    /** Handles play button click, transitioning to a new scene. */
    private void handlePlayButtonClick(Stage primaryStage, Pane root) {
        ScaleTransition stClick = new ScaleTransition(Duration.millis(100));
        stClick.setToX(1.0);
        stClick.setToY(1.0);
        stClick.setOnFinished(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                Scene newScene = createGameScene(primaryStage);
                primaryStage.setScene(newScene);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newScene.getRoot());
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        });
        stClick.play();
    }

    /** Creates the game scene. */
    private Scene createGameScene(Stage primaryStage) {
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color: #A9EDFE;");
        pane.getChildren().add(new Button("Simple Button"));
        return new Scene(pane, 600, 400);
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

    /** Adds hover effect to a button. */
    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #C2EBCA; -fx-text-fill: grey;"));
        button.setOnMouseExited(e -> button.setStyle(""));
    }

    /** Adds a pulsing text effect to the quit button. */
    private void addQuitButtonEffect(Button quitButton) {
        Timeline chargeEffect = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(quitButton.textFillProperty(), Color.LIGHTGRAY)),
                new KeyFrame(Duration.millis(400), new KeyValue(quitButton.textFillProperty(), Color.WHITE))
        );
        chargeEffect.setAutoReverse(true);
        chargeEffect.setCycleCount(Animation.INDEFINITE);
        quitButton.setOnMouseEntered(e -> chargeEffect.play());
        quitButton.setOnMouseExited(e -> chargeEffect.stop());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
