package com.game.event.listener;

import com.game.event.scroll.ScrollEvent;

public interface ScrollEventListener extends EventListener {
    void event(ScrollEvent scrollEvent);
}
