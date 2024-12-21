package com.game.event.window.listener;

import com.game.lwjgl.event.WindowEvents;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Cascade delegation of Lwjgl events to child event listeners<br>
 * Only window is registered as listener of events from Lwjgl<br>
 * <ul>
 * <li>Window: registerWindowEventListener(window)</li>
 * <li>Screen: subscribeOnWindowEvents(camera)</li>
 * <li>CameraEventHandler: subscribeOnWindowEvents(cameraEventHandler)</li>
 * </ul>
 */
public abstract class AbstractWindowEventListener implements WindowEventListener {
    private final List<WindowEventListener> windowEventListeners = new CopyOnWriteArrayList<>();
    protected WindowEvents windowEvents;

    @Override
    public List<WindowEventListener> getEventDelegates() {
        return windowEventListeners;
    }

    public void subscribeOnWindowEvents(WindowEventListener windowEventListener) {
        windowEventListeners.add(windowEventListener);
    }

    public void registerWindowEventListener(EventListener eventListener) {
        if (windowEvents == null) {
            throw new IllegalStateException("Window is not initialized");
        }
        windowEvents.addEventListener(eventListener);
    }

    public void processPendingEvents() {
        windowEvents.processPendingEvents();
    }
}
