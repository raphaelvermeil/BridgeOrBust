package com.example.GUI;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;/*
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;*/

public class SettingsScene {

    private Slider systemVolumeSlider;
    private Slider musicVolumeSlider;
    private CheckBox fullscreenCheckbox;

    public Scene createSettingsScene(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #A9EDFE;");
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        // System Volume Control
        HBox systemVolumeBox = createVolumeControl("System Volume", "system-volume-slider");
        systemVolumeSlider = (Slider) systemVolumeBox.lookup(".slider");
        setupSystemVolumeControl();

        // Music Volume Control
        HBox musicVolumeBox = createVolumeControl("Music Volume", "music-volume-slider");
        musicVolumeSlider = (Slider) musicVolumeBox.lookup(".slider");
        // TODO: Implement music volume control when music system is added

        // Fullscreen Checkbox
        fullscreenCheckbox = new CheckBox("Fullscreen");
        fullscreenCheckbox.setStyle("-fx-font-size: 16px;");
        fullscreenCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            primaryStage.setFullScreen(newVal);
        });

        // Back button
        Button backButton = new Button("BACK");
        backButton.getStyleClass().add("menu-button");
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

        // Add all components to root
        root.getChildren().addAll(
                systemVolumeBox,
                musicVolumeBox,
                fullscreenCheckbox,
                backButton
        );

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("file:style.css");
        return scene;
    }

    /** Creates a volume control with label and slider */
    private HBox createVolumeControl(String labelText, String sliderStyleClass) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);

        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 16px;");

        Slider slider = new Slider(0, 100, 50);
        slider.getStyleClass().add(sliderStyleClass);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        box.getChildren().addAll(label, slider);
        return box;
    }

    /** Sets up system volume control */
    private void setupSystemVolumeControl() {
     /*   try {
            // Get the default mixer
            Mixer mixer = AudioSystem.getMixer(null);

            // Check if volume control is supported
            if (mixer.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                Line.Info[] lineInfos = mixer.getTargetLineInfo();

                systemVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                    // TODO: Implement precise volume control
                    // This is a placeholder and might need more sophisticated implementation
                    System.out.println("System Volume: " + newVal.doubleValue());
                });
            }
        } catch (Exception e) {
            System.err.println("Could not access system volume: " + e.getMessage());
        }*/
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
}