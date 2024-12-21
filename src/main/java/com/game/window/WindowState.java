package com.game.window;

public class WindowState {
    private final String name = "GAME_WINDOW";
    private int swapInterval = 1;
    private boolean isFullScreen = false;
    private int width = 500;
    private int height = 500;

    public void setSwapInterval(int swapInterval) {
        this.swapInterval = swapInterval;
    }

    public int getSwapInterval() {
        return swapInterval;
    }

    public String getName() {
        return name;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
