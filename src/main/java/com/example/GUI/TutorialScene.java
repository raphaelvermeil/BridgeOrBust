package com.example.GUI;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TutorialScene {
    public Scene createScene(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #112233;");
        Label label = new Label("Tutorial to be done... je fais ca prochainement...");
        label.setTextFill(Color.WHITE);
        root.getChildren().add(label);
        return new Scene(root, 1000, 600);
    }
}
