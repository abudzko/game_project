package com.game.window;

import com.game.input.events.EventFactory;
import com.game.input.events.KeyEvent;
import com.game.input.events.MouseEvent;
import com.game.input.events.ScrollEvent;
import com.game.input.events.WindowResizeEvent;
import com.game.model.Camera;
import com.game.utils.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.glViewport;

public class WindowBuilder {
    private int width;
    private int height;
    private int positionX;
    private int positionY;
    private String name;
    private long monitorId;
    private boolean fullScreen;
    private int swapInterval;

    private Camera camera;

    public static WindowBuilder newBuilder() {
        return new WindowBuilder();
    }

    public Window build() {
        Window window = new Window();
        window.setWidth(width);
        window.setHeight(height);
        window.setName(name);
        window.setMonitorId(monitorId);
        window.setFullScreen(fullScreen);
        window.setPositionX(positionX);
        window.setPositionY(positionY);
        window.setSwapInterval(swapInterval);
        window.init();

        initEventCallbacks(window);

        WindowRenderer windowRenderer = new WindowRenderer(window);
        window.setWindowRenderer(windowRenderer);

        window.setCamera(camera);

        return window;
    }

    public WindowBuilder width(int width) {
        this.width = width;
        return this;
    }

    public WindowBuilder height(int height) {
        this.height = height;
        return this;
    }

    public WindowBuilder positionX(int positionX) {
        this.positionX = positionX;
        return this;
    }

    public WindowBuilder positionY(int positionY) {
        this.positionY = positionY;
        return this;
    }

    public WindowBuilder name(String name) {
        this.name = name;
        return this;
    }

    public WindowBuilder monitorId(long monitorId) {
        this.monitorId = monitorId;
        return this;
    }

    public WindowBuilder fullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
        return this;
    }

    public WindowBuilder setSwapInterval(int swapInterval) {
        this.swapInterval = swapInterval;
        return this;
    }

    public WindowBuilder setCamera(Camera camera) {
        this.camera = camera;
        return this;
    }

    private void initEventCallbacks(final Window window) {
        long windowId = window.getWindowId();
        GLFW.glfwSetKeyCallback(windowId, new GLFWKeyCallback() {
            @Override
            public void invoke(long windowId, int key, int scanCode, int action, int mods) {
                KeyEvent keyEvent = EventFactory.buildKeyEvent(key, scanCode, action, mods);
                window.processKeyEvent(keyEvent);
            }
        });

        final ByteBuffer cursorX = BufferUtils.createByteBuffer(new byte[8]);
        final ByteBuffer cursorY = BufferUtils.createByteBuffer(new byte[8]);
        GLFW.glfwSetMouseButtonCallback(windowId, new GLFWMouseButtonCallback() {

            @Override
            public void invoke(long windowId, int button, int action, int mods) {
                GLFW.glfwGetCursorPos(windowId, cursorX, cursorY);
                double x = cursorX.getDouble();
                double y = cursorY.getDouble();

                MouseEvent mouseEvent = EventFactory.buildMouseEvent(button, action, mods, x, y);
                window.processMouseEvent(mouseEvent);

                cursorX.flip();
                cursorY.flip();
            }
        });

        GLFW.glfwSetScrollCallback(windowId, new GLFWScrollCallback() {
            @Override
            public void invoke(long windowId, double offsetX, double offsetY) {
                ScrollEvent scrollEvent = new ScrollEvent(offsetX, offsetY);
                window.processScrollEvent(scrollEvent);
            }
        });
        GLFW.glfwSetCursorPosCallback(windowId, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long windowId, double xpos, double ypos) {

            }
        });

        GLFW.glfwSetCursorEnterCallback(windowId, new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long windowId, int entered) {

            }
        });

        GLFW.glfwSetWindowSizeCallback(windowId, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long windowId, int newWidth, int newHeight) {
                glViewport(0, 0, newWidth, newHeight);
                WindowResizeEvent windowResizeEvent = EventFactory.buildWindowResizeEvent(newWidth, newHeight);
                window.processWindowResizeEvent(windowResizeEvent);
            }
        });
    }
}
