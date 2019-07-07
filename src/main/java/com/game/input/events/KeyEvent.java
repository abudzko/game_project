package com.game.input.events;

public class KeyEvent {
    private final int key;
    private final int scanCode;
    private final int action;
    private final int mods;

    public KeyEvent(int key, int scanCode, int action, int mods) {
        this.key = key;
        this.scanCode = scanCode;
        this.action = action;
        this.mods = mods;
    }

    public int getKey() {
        return key;
    }

    public int getScanCode() {
        return scanCode;
    }

    public int getAction() {
        return action;
    }

    public int getMods() {
        return mods;
    }
}
