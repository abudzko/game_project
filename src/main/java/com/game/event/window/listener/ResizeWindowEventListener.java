package com.game.event.window.listener;

import com.game.event.window.resize.ResizeWindowEvent;

public interface ResizeWindowEventListener extends EventListener {
    default void event(ResizeWindowEvent resizeWindowEvent) {
    }
}
