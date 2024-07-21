package com.game.window;

public class WindowConfig {
    private final boolean isFullScreen = false;
    private final int defaultWidth = 500;
    private final int defaultHeight = 500;

    private float defaultFov = (float) Math.toRadians(50f);

    private float defaultZNear = 0.05f;
    private float defaultZFar = 20.f;
    private String windowName = "GAME_WINDOW";

    private int swapInterval = 1;

    protected float cameraMoveStep = 0.1f;

    public float getCameraMoveStep() {
        return cameraMoveStep;
    }

    public int getSwapInterval() {
        return swapInterval;
    }

    public float getDefaultFov() {
        return defaultFov;
    }

    public float getDefaultZNear() {
        return defaultZNear;
    }

    public float getDefaultZFar() {
        return defaultZFar;
    }

    public String getWindowName() {
        return windowName;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public int getDefaultWidth() {
        return defaultWidth;
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }
}
