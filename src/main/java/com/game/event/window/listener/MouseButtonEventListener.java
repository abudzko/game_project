package com.game.event.window.listener;

import com.game.event.window.mouse.MouseButtonEvent;

public interface MouseButtonEventListener extends EventListener {
    default void event(MouseButtonEvent mouseButtonEvent) {
    }
}
