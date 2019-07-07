package com.game.input.events;

public class WindowResizeEvent {

    private final int newWidth;
    private final int newHeight;

    public WindowResizeEvent(int newWidth, int newHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    public int getNewWidth() {
        return newWidth;
    }

    public int getNewHeight() {
        return newHeight;
    }
}
