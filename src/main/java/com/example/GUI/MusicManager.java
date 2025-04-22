package com.example.GUI;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer mediaPlayer;
    private static boolean isMuted = false;
    private static double currentVolume = 0.5;

    public static void startBackgroundMusic() {
        if (mediaPlayer == null) {
            Media media = new Media(MusicManager.class.getResource("/sounds/Waltz.mp3").toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(currentVolume);
            mediaPlayer.play();
        }
    }

    public static void setVolume(double volume) {
        currentVolume = volume;
        if (mediaPlayer != null && !isMuted) {
            mediaPlayer.setVolume(volume);
        }
    }

    public static void mute() {
        if (mediaPlayer != null) {
            isMuted = true;
            mediaPlayer.setVolume(0);
        }
    }

    public static void unmute() {
        if (mediaPlayer != null) {
            isMuted = false;
            mediaPlayer.setVolume(currentVolume);
        }
    }

    public static boolean isMuted() {
        return isMuted;
    }

    public static double getCurrentVolume() {
        return currentVolume;
    }
}
