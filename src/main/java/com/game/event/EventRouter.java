package com.game.event;

import com.game.event.engine.EngineEventListener;
import com.game.event.window.listener.WindowEventListener;

import java.util.List;

public class EventRouter implements WindowEventListener, EngineEventListener {

    @Override
    public List<WindowEventListener> getEventDelegates() {
        throw new UnsupportedOperationException();
    }
}
