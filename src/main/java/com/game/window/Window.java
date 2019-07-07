package com.game.window;

import com.game.input.WindowEventListener;
import com.game.input.events.KeyEvent;
import com.game.input.events.ScrollEvent;
import com.game.input.events.WindowResizeEvent;
import com.game.model.Camera;
import com.game.model.Model;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements WindowEventListener {
    private long windowId;

    private int width;
    private int height;
    private int positionX;
    private int positionY;

    private final ConcurrentHashMap<Integer, Model> modelMap = new ConcurrentHashMap<>();
    private  HashMap<Integer, DrawableModel> drawableModels = new HashMap<>();

    private Camera camera;
    private WindowRenderer windowRenderer;

    private String name;

    private long monitorId;
    private boolean fullScreen = false;

    private int swapInterval = 1;

    public Window() {
    }

    public void init() {
        setWindowHints();
        windowId = GLFW.glfwCreateWindow(
                width,
                height,
                name,
                fullScreen ? monitorId : NULL,
                NULL
        );

        if (windowId == NULL) {
            GLFW.glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        if (!fullScreen) {
            GLFW.glfwSetWindowPos(windowId, positionX, positionY);
        }
        GLFW.glfwMakeContextCurrent(windowId);
        GL.createCapabilities();

//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

//        GLFW.glfwSwapInterval(swapInterval);
//        GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
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
        windowRenderer.render(drawableModels.values());
    }

    private void readFreshModels() {
        if (modelMap.size() > 0) {//TODO rework
            Iterator<Model> iterator = modelMap.values().iterator();
            while (iterator.hasNext()) {
                Model model = iterator.next();
                drawableModels.put(model.getId(), windowRenderer.createDrawableModel(model));
                iterator.remove();
            }
        }
    }

    public long getWindowId() {
        return windowId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(windowId);
    }

    public void show() {
        GLFW.glfwShowWindow(windowId);
    }

    public void hide() {
        GLFW.glfwHideWindow(windowId);
    }

    public boolean shouldBeClosed() {
        return glfwWindowShouldClose(windowId) == GLFW.GLFW_TRUE;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public void setMonitorId(long monitorId) {
        this.monitorId = monitorId;
    }

    public void setSwapInterval(int swapInterval) {
        this.swapInterval = swapInterval;
    }

    public String getName() {
        return name;
    }

    public void setWindowRenderer(WindowRenderer windowRenderer) {
        this.windowRenderer = windowRenderer;
    }

    @Override
    public void processKeyEvent(KeyEvent keyEvent) {
        float step = 0.1f;
        boolean cameraMoved = false;

        switch (keyEvent.getAction()) {

            case GLFW.GLFW_PRESS:
                switch (keyEvent.getKey()) {
                    case GLFW.GLFW_KEY_UP:
                        camera.moveY(-step);
                        cameraMoved = true;
                        break;
                    case GLFW.GLFW_KEY_DOWN:
                        camera.moveY(step);
                        cameraMoved = true;
                        break;
                    case GLFW.GLFW_KEY_RIGHT:
                        camera.moveX(step);
                        cameraMoved = true;
                        break;
                    case GLFW.GLFW_KEY_LEFT:
                        camera.moveX(-step);
                        cameraMoved = true;
                        break;
                    case GLFW.GLFW_KEY_ESCAPE:
                        GLFW.glfwSetWindowShouldClose(windowId, GLFW.GLFW_TRUE);
                        break;
                    default:
                        cameraMoved = false;
                        break;
                }
                break;
            case GLFW.GLFW_REPEAT:
                switch (keyEvent.getKey()) {
                    case GLFW.GLFW_KEY_UP:
                        camera.moveY(-step);
                        cameraMoved = true;
                        break;
                    case GLFW.GLFW_KEY_DOWN:
                        camera.moveY(step);
                        cameraMoved = true;
                        break;
                    case GLFW.GLFW_KEY_RIGHT:
                        camera.moveX(step);
                        cameraMoved = true;
                        break;
                    case GLFW.GLFW_KEY_LEFT:
                        camera.moveX(-step);
                        cameraMoved = true;
                        break;
                    default:
                        break;
                }
        }

        if (cameraMoved) {
            cameraChanged();
        }
    }

    private void cameraChanged() {
        windowRenderer.onCameraChanged(camera);
    }

    @Override
    public void processScrollEvent(ScrollEvent scrollEvent) {
        float step = -0.1f;
        camera.moveX((int) scrollEvent.getOffsetX() * step);
        camera.moveY((int) scrollEvent.getOffsetY() * step);
        windowRenderer.onCameraChanged(camera);
    }


    public void setCamera(Camera camera) {
        this.camera = camera;
        cameraChanged();
    }

    @Override
    public void processWindowResizeEvent(WindowResizeEvent windowResizeEvent) {
        windowRenderer.setAR((float) windowResizeEvent.getNewWidth() / (float) windowResizeEvent.getNewHeight());
    }

    public void addModel(Model model) {
        modelMap.put(model.getId(), model);
    }
}
