package com.game.event.window.listener;

import com.game.event.window.scroll.ScrollEvent;

public interface ScrollEventListener extends EventListener {
    default void event(ScrollEvent scrollEvent) {
    }
}
