package com.game.window;

import com.game.model.Camera;
import org.lwjgl.glfw.GLFWVidMode;

public class WindowFactory {

    public static final String MAIN_WINDOW_NAME = "main";

    public static Window buildMainWindow(Monitor monitor) {
        int width = 500;
        int height = 500;
        GLFWVidMode videoMode = monitor.getVideoMode();
        int positionX = (videoMode.width() - width) / 2;
        int positionY = (videoMode.height() - height) / 2;
        Camera camera = new Camera();
        Window window = WindowBuilder.newBuilder()
                .width(width)
                .height(height)
                .name(MAIN_WINDOW_NAME)
                .monitorId(monitor.getMonitorId())
                .fullScreen(false)
                .positionX(positionX)
                .positionY(positionY)
                .setSwapInterval(1)
                .setCamera(camera)
                .build();
        return window;
    }
}
