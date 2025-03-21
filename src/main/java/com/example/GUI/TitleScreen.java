/*
package com.example.GUI;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TitleScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #A9EDFE;"); // Cyan background;

        // Create buttons playButton
        Button playButton = createStyledButton("PLAY", 40, 70);
        playButton.setRotate(-19);
        addHoverEffect(playButton);

        Button settingsButton = createStyledButton("SETTINGS", 30, 190);
        settingsButton.setRotate(8);
        addHoverEffect(settingsButton);

        Button quitButton = createStyledButton("QUIT", 40, 310);
        quitButton.setRotate(-4);
        addHoverEffect(quitButton);
        // Create "hanging" lines
        Line line1 = new Line(150, 0, 60, 90); // first line
        Line line2 = new Line(150,0,165,60); //second line
        Line line3 = new Line(75, 125, 69, 200); // third lane
        Line line4 = new Line(180, 115, 200, 220); // fourth lane
        Line line5 = new Line(57,240,63,315);
        Line line6 = new Line(206,250,167,310);

        Circle circle19 = new Circle(25, 8, 30);
        circle19.setFill(Color.WHITESMOKE);

        Circle circle15 = new Circle(35, 11, 34);
        circle15.setFill(Color.WHITESMOKE);

        Circle circle20 = new Circle(45, 15, 32);
        circle20.setFill(Color.WHITESMOKE);

        Circle circle16 = new Circle(55, 19, 35);
        circle16.setFill(Color.WHITE);

        Circle circle17 = new Circle(65, 23, 37);
        circle17.setFill(Color.WHITE);

        Circle circle18 = new Circle(75,22,36);
        circle18.setFill(Color.WHITESMOKE);

        Circle circle3 = new Circle(85, 26, 34);
        circle3.setFill(Color.WHITESMOKE);

        Circle circle4 = new Circle(95, 21, 32);
        circle4.setFill(Color.WHITE);

        Circle circle5 = new Circle(105, 17, 36);
        circle5.setFill(Color.WHITESMOKE);

        Circle circle6 = new Circle(115, 14, 34);
        circle6.setFill(Color.WHITESMOKE);

        Circle circle7 = new Circle(125, 11, 33);
        circle7.setFill(Color.WHITESMOKE);

        Circle circle8 = new Circle(135, 7, 27);
        circle8.setFill(Color.WHITESMOKE);

        Circle circle9 = new Circle(145, 4, 22);
        circle9.setFill(Color.WHITESMOKE);

        Circle circle10 = new Circle(155, 3, 21);
        circle10.setFill(Color.WHITESMOKE);

        Circle circle11 = new Circle(450, 8, 24);
        circle11.setFill(Color.FLORALWHITE);

        Circle circle12 = new Circle(480, 8, 36);
        circle12.setFill(Color.FLORALWHITE);

        Circle circle13 = new Circle(520, 15, 40);
        circle13.setFill(Color.WHITESMOKE);

        Circle circle14 = new Circle(560, 12, 26);
        circle14.setFill(Color.WHITESMOKE);

        ImageView logo = new ImageView(new Image("file:logo.png"));
        logo.setX(250);
        logo.setY(95);
        logo.setFitWidth(285);  // Resize if needed
        logo.setFitHeight(330);

        ImageView bridge = new ImageView(new Image("file:bridge.PNG"));
        bridge.setX(240);
        bridge.setY(95);
        bridge.setFitWidth(405);
        bridge.setFitHeight(320);
       *//*

*/
/* bridge.setX(-4);
        bridge.setY(15);
        bridge.setFitWidth(250);  // Resize if needed
        bridge.setFitHeight(300);
        bridge.setRotate(-2);*//*
*/
/*


        //testing effect 1
        ScaleTransition stEnter = new ScaleTransition(Duration.millis(200), playButton);
        stEnter.setToX(1.3);
        stEnter.setToY(1.2);

        ScaleTransition stExit = new ScaleTransition(Duration.millis(200), playButton);
        stExit.setToX(1.0);
        stExit.setToY(1.0);

        playButton.setOnMouseEntered(e -> stEnter.playFromStart());
        playButton.setOnMouseExited(e -> stExit.playFromStart());

        //testing effects 2
        playButton.setOnMousePressed(e -> {
            playButton.setStyle("-fx-background-color: grey;");
        });
        playButton.setOnMouseReleased(e -> {
            playButton.setStyle("");  // Reset to default
        });

        //testing effect 3
        Timeline chargeEffect = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(quitButton.textFillProperty(), Color.LIGHTGRAY)),
                new KeyFrame(Duration.millis(400), new KeyValue(quitButton.textFillProperty(), Color.WHITE))
        );
        chargeEffect.setAutoReverse(true);
        chargeEffect.setCycleCount(Animation.INDEFINITE);

        quitButton.setOnMouseEntered(e -> chargeEffect.play());
        quitButton.setOnMouseExited(e -> chargeEffect.stop());


        // Add elements to root
        root.getChildren().addAll(line1, line2, line3, line4, line5, line6,  bridge,
                playButton, settingsButton, quitButton, circle3, circle4,circle5,circle6,circle7,circle8,
                circle9,circle10,
                circle11,circle12,circle13,circle14,
                circle15, circle16,circle17,circle18,circle19,circle20);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Swinging Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add("file:style.css");

    }

    private Button createStyledButton(String text, double x, double y) {
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.getStyleClass().add("menu-button");
        return button;
    }
    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #C2EBCA; -fx-text-fill: grey;")); // color on hover
        button.setOnMouseExited(e -> button.setStyle("")); // Reset to default style
    }
    public static void main(String[] args) {
        launch(args);
    }
}
*//*

package com.example.GUI;

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
import javafx.stage.Stage;
import javafx.util.Duration;

public class TitleScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #A9EDFE;"); // Background color

        // Create buttons
        Button playButton = createStyledButton("PLAY", 40, 70);
        playButton.setRotate(-19);
        addHoverEffect(playButton);
        
        Button settingsButton = createStyledButton("SETTINGS", 30, 190);
        settingsButton.setRotate(8);
        addHoverEffect(settingsButton);

        Button quitButton = createStyledButton("QUIT", 40, 310);
        quitButton.setRotate(-4);
        addHoverEffect(quitButton);

        // Set quit button to exit the program when clicked
        quitButton.setOnAction(e -> Platform.exit());

        // Create "hanging" lines
        // DELETED CODE (ignored as per your instruction)

        // Add images
        ImageView logo = new ImageView(new Image("file:logo.png"));
        logo.setX(250);
        logo.setY(95);
        logo.setFitWidth(285);  // Resize if needed
        logo.setFitHeight(330);

        ImageView bridge = new ImageView(new Image("file:bridge.PNG"));
        bridge.setX(240);
        bridge.setY(95);
        bridge.setFitWidth(405);
        bridge.setFitHeight(320);

        // Play button scale transitions for mouse hover
        ScaleTransition stEnter = new ScaleTransition(Duration.millis(200), playButton);
        stEnter.setToX(1.3);
        stEnter.setToY(1.2);

        ScaleTransition stExit = new ScaleTransition(Duration.millis(200), playButton);
        stExit.setToX(1.0);
        stExit.setToY(1.0);

        playButton.setOnMouseEntered(e -> stEnter.playFromStart());
        playButton.setOnMouseExited(e -> stExit.playFromStart());

        // Additional effects for playButton (color change on press)
        playButton.setOnMousePressed(e -> playButton.setStyle("-fx-background-color: grey;"));
        playButton.setOnMouseReleased(e -> playButton.setStyle(""));

        // Quit button text color cycling effect
        Timeline chargeEffect = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(quitButton.textFillProperty(), Color.LIGHTGRAY)),
                new KeyFrame(Duration.millis(400), new KeyValue(quitButton.textFillProperty(), Color.WHITE))
        );
        chargeEffect.setAutoReverse(true);
        chargeEffect.setCycleCount(Animation.INDEFINITE);
        quitButton.setOnMouseEntered(e -> chargeEffect.play());
        quitButton.setOnMouseExited(e -> chargeEffect.stop());

        // Play button action: on click, quickly scale to normal and then change scene with transitions.
        playButton.setOnAction(e -> {
            ScaleTransition stClick = new ScaleTransition(Duration.millis(100), playButton);
            stClick.setToX(1.0);
            stClick.setToY(1.0);
            stClick.setOnFinished(event -> {
                // Fade out current scene
                FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(ev -> {
                    // Create and set the new scene
                    Scene newScene = createGameScene(primaryStage);
                    primaryStage.setScene(newScene);
                    // Fade in new scene for a modern effect
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newScene.getRoot());
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();
                });
                fadeOut.play();
            });
            stClick.play();
        });

        // Add elements to root. (The deleted lines and additional nodes are assumed.)
        root.getChildren().addAll(
                // line1, line2, ... (DELETED CODE)
                bridge, playButton, settingsButton, quitButton, logo
                // , additional circles or nodes from deleted code
        );

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Swinging Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add("file:style.css");
    }

    */
/**
     * Creates the new game scene with a simple centered button and background color #A9EDFE.
     *//*

    private Scene createGameScene(Stage primaryStage) {
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color: #A9EDFE;");
        Button simpleButton = new Button("Simple Button");
        pane.getChildren().add(simpleButton);
        return new Scene(pane, 600, 400);
    }

    */
/**
     * Helper method to create a styled button.
     *//*

    private Button createStyledButton(String text, double x, double y) {
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.getStyleClass().add("menu-button");
        return button;
    }

    */
/**
     * Helper method to add a hover effect to a button.
     *//*

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #C2EBCA; -fx-text-fill: grey;"));
        button.setOnMouseExited(e -> button.setStyle(""));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

*/
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

public class TitleScreen extends Application {
//c parfait encore parfait encore
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #A9EDFE;"); // Background color

        // Load UI elements
        addHangingLines(root);
        addCircles(root);
        addImages(root);
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
    private void addImages(Pane root) {
        ImageView logo = new ImageView(new Image("file:logo.png"));
        logo.setX(250);
        logo.setY(95);
        logo.setFitWidth(285);
        logo.setFitHeight(330);

        ImageView bridge = new ImageView(new Image("file:bridge.PNG"));
        bridge.setX(240);
        bridge.setY(95);
        bridge.setFitWidth(405);
        bridge.setFitHeight(320);

        root.getChildren().addAll(bridge, logo);
    }

    /** Adds buttons to the UI and assigns functionality. */
    private void addButtons(Pane root, Stage primaryStage) {
        Button playButton = createStyledButton("PLAY", 40, 70, -19);
        Button settingsButton = createStyledButton("SETTINGS", 30, 190, 8);
        Button quitButton = createStyledButton("QUIT", 40, 310, -4);

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
