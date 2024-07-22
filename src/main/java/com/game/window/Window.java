package com.game.window;

import com.game.model.GameUnit;
import com.game.shader.DrawableModel;
import com.game.shader.Program;
import com.game.utils.log.LogUtil;
import com.game.utils.math.Matrix4f;
import com.game.window.events.WindowEvents;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final WindowConfig windowConfig;
    private final Camera camera;
    private final ConcurrentHashMap<Long, GameUnit> modelMap = new ConcurrentHashMap<>();
    private final HashMap<Long, DrawableModel> drawableModels = new HashMap<>();
    private int width;
    private int height;
    private long windowId;
    private Program program;

    public Window(WindowConfig windowConfig) {
        this.windowConfig = windowConfig;
        this.width = windowConfig.getDefaultWidth();
        this.height = windowConfig.getDefaultHeight();
        this.camera = new Camera();
    }

    public void start() throws InterruptedException {
        var countDownLaunch = new CountDownLatch(1);
        var windowRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // All lwjgl calls "This function must only be called from the main thread." should be done in same Thread
                    init();
                    show();
                    createProgram();
                    countDownLaunch.countDown();
                    while (!shouldBeClosed()) {
                        GLFW.glfwPollEvents();
                        render();
                    }
                } catch (RuntimeException e) {
                    LogUtil.logError(String.format("Error happened inside window [%s] thread. %s", windowId, e.getMessage()));
                } finally {
                    destroy();
                }
            }
        };

        var windowThread = new Thread(windowRunnable);
        windowThread.start();
        // Wait while window will be initialized
        countDownLaunch.await();
    }

    private void init() {
        // Set an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        var monitorId = GLFW.glfwGetPrimaryMonitor();
        var videoMode = GLFW.glfwGetVideoMode(monitorId);
        monitorId = NULL;
        assert videoMode != null;

        setWindowHints();

        windowId = org.lwjgl.glfw.GLFW.glfwCreateWindow(
                width,
                height,
                windowConfig.getWindowName(),
                monitorId,
                NULL
        );

        if (windowId == NULL) {
            GLFW.glfwTerminate();
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        GLFW.glfwMakeContextCurrent(windowId);
        GL.createCapabilities();

        GLFW.glfwSwapInterval(windowConfig.getSwapInterval());

        new WindowEvents(this).attachEvents();
    }

    private void createProgram() {
        program = new Program(windowId);
        var aspectRatio = (float) getWidth() / (float) getHeight();
        updateProjectionMatrix(aspectRatio);
        getProgram().linkProgram();
        cameraChanged();
    }

    private Program getProgram() {
        if (program == null) {
            throw new IllegalStateException(String.format("Program for window [%s] was not created", windowId));
        }
        return program;
    }

    private void setWindowHints() {
        GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
    }

    public void render() {
        readFreshModels();
        getProgram().render(drawableModels.values());
    }

    private void readFreshModels() {
        if (modelMap.size() > 0) {// TODO rework
            var iterator = modelMap.values().iterator();
            while (iterator.hasNext()) {
                var model = iterator.next();
                drawableModels.put(model.getId(), getProgram().createDrawableModel(model));
                iterator.remove();
            }
        }
    }

    public long getWindowId() {
        return windowId;
    }

    private int getWidth() {
        return width;
    }

    private int getHeight() {
        return height;
    }

    private void destroy() {
        GLFW.glfwDestroyWindow(windowId);
    }

    private void show() {
        GLFW.glfwShowWindow(windowId);
    }

    public void hide() {
        GLFW.glfwHideWindow(windowId);
    }

    public boolean shouldBeClosed() {
        return glfwWindowShouldClose(windowId);
    }

    public void windowSizeChanged(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
        var aspectRatio = (float) width / (float) height;
        updateProjectionMatrix(aspectRatio);
        getProgram().cameraViewMatrixChanged(camera.getLookAtMatrix());
    }

    public void moveCameraX(float step) {
        camera.moveX(step);
        cameraChanged();
    }

    public void moveCameraY(float step) {
        camera.moveY(step);
        cameraChanged();
    }

    public void moveCameraZ(float step) {
        camera.moveZ(step);
        cameraChanged();
    }

    private void cameraChanged() {
        getProgram().cameraViewMatrixChanged(camera.getLookAtMatrix());
    }

    public void addModel(GameUnit gameUnit) {
        modelMap.put(gameUnit.getId(), gameUnit);
    }

    private void updateProjectionMatrix(float aspectRatio) {
        var projectionMatrix = new Matrix4f();
        projectionMatrix.perspective(
                windowConfig.getDefaultFov(),
                aspectRatio,
                windowConfig.getDefaultZNear(),
                windowConfig.getDefaultZFar()
        );
        getProgram().projectionMatrixChanged(projectionMatrix);
    }

    public WindowConfig getWindowConfig() {
        return windowConfig;
    }
}
