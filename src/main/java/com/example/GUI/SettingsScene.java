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

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

public class SettingsScene {

    private Slider systemVolumeSlider;
    private Slider musicVolumeSlider;
    private CheckBox fullscreenCheckbox;

    public Scene createSettingsScene(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #5ed3f7ff");
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);




        // Music volume Control
        HBox musicVolumeBox = createVolumeControl("Music Volume", "music-volume-slider");
        musicVolumeSlider = (Slider) musicVolumeBox.lookup(".slider");
        musicVolumeSlider.setValue(com.example.GUI.MusicManager.isMuted() ? 0 : com.example.GUI.MusicManager.getCurrentVolume() * 100);

        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue() / 100.0;
            System.out.println("Volume changed to: " + volume);
            com.example.GUI.MusicManager.setVolume(volume);
        });



        CheckBox muteMusicCheckbox = new CheckBox("Mute Music");
        muteMusicCheckbox.setStyle("-fx-font-size: 16px;");
        muteMusicCheckbox.setSelected(com.example.GUI.MusicManager.isMuted());

        muteMusicCheckbox.selectedProperty().addListener((obs, wasMuted, isNowMuted) -> {
            if (isNowMuted) {
                com.example.GUI.MusicManager.mute();
            } else {
                com.example.GUI.MusicManager.unmute();
            }
        });




        // Back button
        Button backButton = new Button("BACK");
        backButton.setStyle("-fx-text-fill: #006699; -fx-background-color: white; -fx-font-size: 28px;");
        backButton.getStyleClass().add("menu-button");
        addHoverEffect(backButton);



        backButton.setOnAction(e -> {
            GameTitleScreen gameTitleScreen = new GameTitleScreen();
            Scene scene = gameTitleScreen.createGameTitleScene(primaryStage);
            primaryStage.setScene(scene);
        });


        // Add all component
        root.getChildren().addAll(
                musicVolumeBox,
                muteMusicCheckbox,
        //        fullscreenCheckbox,
                backButton
        );


        Scene scene = new Scene(root, 1000, 600);
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