package com.game.event.window.listener;

import com.game.event.window.key.KeyEvent;

public interface KeyEventListener extends EventListener {
    default void event(KeyEvent keyEvent) {
    }
}
