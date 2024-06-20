package com.game.window;

import com.game.model.Camera;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowFactory {

    public static final String MAIN_WINDOW_NAME = "main";

    public static Window buildMainWindow(Monitor monitor) {
        int width;
        int height;
        int positionX;
        int positionY;
        long monitorId = NULL;
        GLFWVidMode videoMode = monitor.getVideoMode();
        Camera camera = new Camera();
        boolean fullScreen = false;
        if (fullScreen) {
            width = videoMode.width();
            height = videoMode.height();
            positionX = 0;
            positionY = 0;
            monitorId = monitor.getMonitorId();
        } else {
            width = 500;
            height = 500;
            positionX = (videoMode.width() - width) / 2;
            positionY = (videoMode.height() - height) / 2;
        }

        Window window = WindowBuilder.newBuilder()
                .width(width)
                .height(height)
                .name(MAIN_WINDOW_NAME)
                .monitorId(monitorId)
                .positionX(positionX)
                .positionY(positionY)
                .setSwapInterval(1)
                .setCamera(camera)
                .build();
        return window;
    }
}
