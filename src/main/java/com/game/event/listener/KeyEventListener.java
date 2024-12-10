package com.game.event.listener;

import com.game.event.key.KeyEvent;

public interface KeyEventListener  extends EventListener{
    void event(KeyEvent keyEvent);
}
