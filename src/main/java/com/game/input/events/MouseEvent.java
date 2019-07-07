package com.game.input.events;

public class MouseEvent {
    private final int button;
    private final int action;
    private final int mods;
    private final double x;
    private final double y;

    public MouseEvent(int button, int action, int mods, double x, double y) {
        this.button = button;
        this.action = action;
        this.mods = mods;
        this.x = x;
        this.y = y;
    }

    public int getButton() {
        return button;
    }

    public int getAction() {
        return action;
    }

    public int getMods() {
        return mods;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
