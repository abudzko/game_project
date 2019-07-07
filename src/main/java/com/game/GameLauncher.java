package com.game;

import com.game.dao.ModelDao;
import com.game.engine.Engine;
import com.game.window.Monitor;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import static com.game.utils.log.LogUtil.log;

public class GameLauncher {

    private GLFWErrorCallback errorCallback;

    private Monitor monitor;

    private Engine engine;

    private ModelDao modelDao;

    public static void main(String[] args) {
        new GameLauncher().launch();
    }

    public void launch() {
        log(String.format("Starting LWJGL: %s version.", Version.getVersion()));
        init();
    }


    private void init() {
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        GLFW.glfwSetErrorCallback(errorCallback);

        if (GLFW.glfwInit() == GLFW.GLFW_FALSE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        monitor = new Monitor();
        engine = new Engine();
        engine.setMonitor(monitor);
        new Thread(engine).start();
    }
}
