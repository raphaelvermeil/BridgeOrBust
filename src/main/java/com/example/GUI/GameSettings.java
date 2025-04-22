package com.example.GUI;

public class GameSettings {
    private static boolean isFullscreen = false;

    public static boolean isFullscreen() {
        return isFullscreen;
    }

    public static void setFullscreen(boolean fullscreen) {
        isFullscreen = fullscreen;
    }
}
