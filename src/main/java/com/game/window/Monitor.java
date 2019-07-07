package com.game.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.concurrent.ConcurrentHashMap;

public class Monitor {
    private final long monitorId;
    private final int width;
    private final int height;
    private final GLFWVidMode videoMode;

    private ConcurrentHashMap<String, Window> windowMap = new ConcurrentHashMap<>();

    public Monitor() {
        monitorId = GLFW.glfwGetPrimaryMonitor();
        videoMode = GLFW.glfwGetVideoMode(monitorId);
        width = videoMode.width();
        height = videoMode.height();
        createWindows();
    }

    private void createWindows() {
        final Monitor monitor = this;
        Runnable mainWindowRunnable = new Runnable() {
            @Override
            public void run() {
                Window mainWindow = WindowFactory.buildMainWindow(monitor);
                windowMap.put(mainWindow.getName(), mainWindow);
                mainWindow.show();
                boolean isRun = true;
                while (isRun) {
                    mainWindow.render();
                    GLFW.glfwPollEvents();
                    if (mainWindow.shouldBeClosed()) {
                        isRun = false;
                 }
                }
            }
        };
        Thread mainWindowThread = new Thread(mainWindowRunnable);
        mainWindowThread.start();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getMonitorId() {
        return monitorId;
    }

    public GLFWVidMode getVideoMode() {
        return videoMode;
    }

    public Window getWindow(String name) {
        return windowMap.get(name);
    }
}
