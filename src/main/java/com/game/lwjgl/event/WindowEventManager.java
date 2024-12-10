package com.game.lwjgl.event;

import com.game.event.cursor.CursorPositionEvent;
import com.game.event.key.KeyEvent;
import com.game.event.listener.CursorPositionEventListener;
import com.game.event.listener.EventListener;
import com.game.event.listener.KeyEventListener;
import com.game.event.listener.MouseButtonEventListener;
import com.game.event.listener.ResizeWindowEventListener;
import com.game.event.listener.ScrollEventListener;
import com.game.event.mouse.MouseButtonEvent;
import com.game.event.resize.ResizeWindowEvent;
import com.game.event.scroll.ScrollEvent;
import com.game.utils.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WindowEventManager {
    private final Long windowId;

    private final List<MouseButtonEventListener> mouseButtonEventListeners = new CopyOnWriteArrayList<>();
    private final List<ScrollEventListener> scrollEventListeners = new CopyOnWriteArrayList<>();
    private final List<KeyEventListener> keyEventListeners = new CopyOnWriteArrayList<>();
    private final List<ResizeWindowEventListener> resizeWindowEventListeners = new CopyOnWriteArrayList<>();
    private final List<CursorPositionEventListener> cursorPositionEventListeners = new CopyOnWriteArrayList<>();

    public WindowEventManager(Long windowId) {
        this.windowId = windowId;
    }

    public void addMouseButtonEventListener(MouseButtonEventListener listener) {
        mouseButtonEventListeners.add(listener);
    }

    public void addScrollEventListener(ScrollEventListener listener) {
        scrollEventListeners.add(listener);
    }

    public void addKeyEventListener(KeyEventListener listener) {
        keyEventListeners.add(listener);
    }

    public void addResizeWindowEventListener(ResizeWindowEventListener listener) {
        resizeWindowEventListeners.add(listener);
    }

    public void addCursorPositionEventListener(CursorPositionEventListener listener) {
        cursorPositionEventListeners.add(listener);
    }

    public void configureEventCallbacks() {
        GLFW.glfwSetKeyCallback(windowId, new GLFWKeyCallback() {
            @Override
            public void invoke(long windowId, int key, int scanCode, int action, int mods) {
                var keyEvent = EventFactory.buildKeyEvent(key, scanCode, action, mods);
                processKeyEvent(keyEvent);
            }
        });

        var cursorX = BufferUtils.createByteBuffer(new byte[8]);
        var cursorY = BufferUtils.createByteBuffer(new byte[8]);
        GLFW.glfwSetMouseButtonCallback(windowId, new GLFWMouseButtonCallback() {

            @Override
            public void invoke(long windowId, int button, int action, int mods) {
                GLFW.glfwGetCursorPos(windowId, cursorX.asDoubleBuffer(), cursorY.asDoubleBuffer());
                var x = cursorX.getDouble();
                var y = cursorY.getDouble();

                var mouseEvent = EventFactory.buildMouseEvent(button, action, mods, x, y);
                processMouseEvent(mouseEvent);

                cursorX.flip();
                cursorY.flip();
            }
        });

        GLFW.glfwSetScrollCallback(windowId, new GLFWScrollCallback() {
            @Override
            public void invoke(long windowId, double offsetX, double offsetY) {
                var scrollEvent = EventFactory.buildScrollEvent(offsetX, offsetY);
                processScrollEvent(scrollEvent);
            }
        });
        GLFW.glfwSetCursorPosCallback(windowId, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long windowId, double x, double y) {
                var cursorPositionEvent = EventFactory.buildCursorPositionEvent(x, y);
                processCursorPositionEvent(cursorPositionEvent);
            }
        });

        GLFW.glfwSetCursorEnterCallback(windowId, new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
            }
        });

        GLFW.glfwSetWindowSizeCallback(windowId, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long windowId, int newWidth, int newHeight) {
                var windowResizeEvent = EventFactory.buildWindowResizeEvent(newWidth, newHeight);
                processWindowResizeEvent(windowResizeEvent);
            }
        });
    }

    private void processCursorPositionEvent(CursorPositionEvent cursorPositionEvent) {
        cursorPositionEventListeners.forEach(listener -> listener.event(cursorPositionEvent));
    }

    private void processKeyEvent(KeyEvent keyEvent) {
        keyEventListeners.forEach(listener -> listener.event(keyEvent));
    }

    private void processScrollEvent(ScrollEvent scrollEvent) {
        scrollEventListeners.forEach(listener -> listener.event(scrollEvent));
    }

    private void processWindowResizeEvent(ResizeWindowEvent resizeWindowEvent) {
        resizeWindowEventListeners.forEach(listener -> listener.event(resizeWindowEvent));
    }

    private void processMouseEvent(MouseButtonEvent mouseButtonEvent) {
        mouseButtonEventListeners.forEach(listener -> listener.event(mouseButtonEvent));
    }

    public void addEventListener(EventListener eventListener) {
        if (eventListener instanceof KeyEventListener) {
            addKeyEventListener((KeyEventListener) eventListener);
        }
        if (eventListener instanceof MouseButtonEventListener) {
            addMouseButtonEventListener((MouseButtonEventListener) eventListener);
        }
        if (eventListener instanceof ResizeWindowEventListener) {
            addResizeWindowEventListener((ResizeWindowEventListener) eventListener);
        }
        if (eventListener instanceof ScrollEventListener) {
            addScrollEventListener((ScrollEventListener) eventListener);
        }
        if (eventListener instanceof CursorPositionEventListener) {
            addCursorPositionEventListener((CursorPositionEventListener) eventListener);
        }
    }

    public void processPendingEvents() {
        GLFW.glfwPollEvents();
    }
}
