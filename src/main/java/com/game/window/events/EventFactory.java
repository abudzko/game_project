package com.game.window.events;

public class EventFactory {
    public static KeyEvent buildKeyEvent(int key, int scanCode, int action, int mods) {
        KeyEvent keyEvent = new KeyEvent(key, scanCode, action, mods);
        return keyEvent;
    }

    public static MouseEvent buildMouseEvent(int button, int action, int mods, double x, double y) {
        MouseEvent mouseEvent = new MouseEvent(button, action, mods, x, y);
        return mouseEvent;
    }

    public static ScrollEvent buildScrollEvent(double offsetX, double offsetY) {
        ScrollEvent scrollEvent = new ScrollEvent(offsetX, offsetY);
        return scrollEvent;
    }

    public static WindowResizeEvent buildWindowResizeEvent(int newWidth, int newHeight) {
        WindowResizeEvent windowResizeEvent = new WindowResizeEvent(newWidth, newHeight);
        return windowResizeEvent;
    }
}
