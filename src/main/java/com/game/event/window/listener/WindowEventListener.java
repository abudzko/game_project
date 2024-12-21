package com.game.event.window.listener;

import com.game.event.window.cursor.CursorPositionEvent;
import com.game.event.window.key.KeyEvent;
import com.game.event.window.mouse.MouseButtonEvent;
import com.game.event.window.resize.ResizeWindowEvent;
import com.game.event.window.scroll.ScrollEvent;

import java.util.Collections;
import java.util.List;

public interface WindowEventListener extends CursorPositionEventListener, KeyEventListener, MouseButtonEventListener,
        ResizeWindowEventListener, ScrollEventListener {

    default List<WindowEventListener> getEventDelegates() {
        return Collections.emptyList();
    }

    @Override
    default void event(CursorPositionEvent event) {
        getEventDelegates().forEach(listener -> listener.event(event));
    }

    @Override
    default void event(KeyEvent event) {
        getEventDelegates().forEach(listener -> listener.event(event));
    }

    @Override
    default void event(MouseButtonEvent event) {
        getEventDelegates().forEach(listener -> listener.event(event));
    }

    @Override
    default void event(ResizeWindowEvent event) {
        getEventDelegates().forEach(listener -> listener.event(event));
    }

    @Override
    default void event(ScrollEvent event) {
        getEventDelegates().forEach(listener -> listener.event(event));
    }
}
