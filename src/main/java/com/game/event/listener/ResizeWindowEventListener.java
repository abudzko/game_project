package com.game.event.listener;

import com.game.event.resize.ResizeWindowEvent;

public interface ResizeWindowEventListener  extends EventListener {
    void event(ResizeWindowEvent resizeWindowEvent);
}
