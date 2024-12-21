package com.game.event.window.listener;

import com.game.event.window.cursor.CursorPositionEvent;

public interface CursorPositionEventListener extends EventListener {
    default void event(CursorPositionEvent cursorPositionEvent) {
    }
}
