package com.game.event.window.key;

public enum KeyActionType {
    RELEASED(0),
    PRESSED(1),
    REPEAT(2);

    private final int key;

    KeyActionType(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
