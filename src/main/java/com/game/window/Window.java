package com.game.window;

import com.game.model.GameUnit;
import com.game.model.DrawableModel;
import com.game.lwjgl.program.Program;
import com.game.utils.log.LogUtil;
import com.game.window.camera.Camera;
import com.game.window.camera.CameraContext;
import com.game.window.camera.CameraEventHandler;
import com.game.window.camera.CameraState;
import com.game.window.camera.world.CameraToWorldConverter;
import com.game.window.camera.world.GroundIntersection;
import com.game.event.resize.ResizeWindowEvent;
import com.game.event.scroll.ScrollEvent;
import com.game.lwjgl.event.WindowEventManager;
import com.game.event.key.KeyEvent;
import com.game.event.listener.EventListener;
import com.game.event.listener.KeyEventListener;
import com.game.event.listener.MouseButtonEventListener;
import com.game.event.listener.ResizeWindowEventListener;
import com.game.event.listener.ScrollEventListener;
import com.game.event.mouse.MouseButtonEvent;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
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

public class Window implements KeyEventListener, MouseButtonEventListener, ScrollEventListener, ResizeWindowEventListener {
    private final WindowConfig windowConfig;
    private final Queue<GameUnit> gameUnits = new ConcurrentLinkedQueue<>();
    private final Map<Long, DrawableModel> drawableModels = new ConcurrentHashMap<>();
    private int width;
    private int height;
    private long windowId;
    private Program program;
    private Camera camera;
    private WindowEventManager windowEventManager;

    public Window(WindowConfig windowConfig) {
        this.windowConfig = windowConfig;
        this.width = windowConfig.getDefaultWidth();
        this.height = windowConfig.getDefaultHeight();
    }

    public void awaitOfLaunch() throws InterruptedException {
        var countDownLaunch = new CountDownLatch(1);
        var windowRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // All lwjgl calls "This function must only be called from the main thread." should be done in same Thread
                    init();
                    show();
                    createProgram();
                    createCamera();
                    countDownLaunch.countDown();
                    while (!shouldBeClosed()) {
                        windowEventManager.processPendingEvents();
                        render();
                    }
                } catch (RuntimeException e) {
                    LogUtil.logError(String.format("Error happened inside window [%s] thread", windowId), e);
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

        windowId = GLFW.glfwCreateWindow(
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

        windowEventManager = new WindowEventManager(getWindowId());
        windowEventManager.configureEventCallbacks();
        addEventListener(this);
    }

    private void createProgram() {
        program = new Program(windowId);
        updateProjectionMatrix();
        getProgram().linkProgram();
    }

    private void createCamera() {
        var cameraContext = new CameraContext();
        cameraContext.setMoveStep(windowConfig.getCameraMoveStep());
        cameraContext.setProgram(getProgram());
        cameraContext.setCameraState(new CameraState());
        this.camera = new Camera(cameraContext);
        CameraEventHandler cameraEventHandler = new CameraEventHandler(cameraContext);
        addEventListener(cameraEventHandler);
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
        while (!gameUnits.isEmpty()) {
            var gameUnit = gameUnits.poll();
            var drawableModel = drawableModels.get(gameUnit.getId());
            if (drawableModel == null) {
                drawableModels.put(gameUnit.getId(), getProgram().createDrawableModel(gameUnit));
            } else {
                drawableModel.updateWorldMatrix();
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

    private void windowSizeChanged(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
        updateProjectionMatrix();
        getProgram().cameraViewMatrixChanged(camera.getLookAtMatrix());
    }

    private void cameraChanged() {
        getProgram().cameraViewMatrixChanged(camera.getLookAtMatrix());
    }

    public void addGameUnit(GameUnit gameUnit) {
        gameUnits.add(gameUnit);
    }

    private void updateProjectionMatrix() {
        var projectionMatrix = createProjectionMatrix();
        getProgram().projectionMatrixChanged(projectionMatrix);
    }

    private Matrix4f createProjectionMatrix() {
        var aspectRatio = (float) getWidth() / (float) getHeight();
        var projectionMatrix = new Matrix4f();
        projectionMatrix.perspective(
                windowConfig.getDefaultFov(),
                aspectRatio,
                windowConfig.getDefaultZNear(),
                windowConfig.getDefaultZFar()
        );
        return projectionMatrix;
    }

    @Override
    public void event(KeyEvent keyEvent) {

    }

    @Override
    public void event(MouseButtonEvent mouseButtonEvent) {
    }

    // TODO ??
    public Vector3f getWorldCoordinates(MouseButtonEvent mouseButtonEvent){
        var projectionMatrix = createProjectionMatrix();
        var c = new CameraToWorldConverter(mouseButtonEvent, projectionMatrix, camera.getLookAtMatrix());
        var rayVector = c.rayVector();
        LogUtil.log(String.valueOf(camera.getLookAtMatrix()));
        var cameraState = camera.getCameraState();
        var cameraPosition = new Vector3f(cameraState.eyeX, cameraState.eyeY, cameraState.eyeZ);
        var wordCoordinates = new GroundIntersection(cameraPosition).findPoint(rayVector);
        LogUtil.log(String.format("wordCoordinates: X = %s, Y = %s, Z = %s", wordCoordinates.x, wordCoordinates.y, wordCoordinates.z));
        return wordCoordinates;
    }

    @Override
    public void event(ResizeWindowEvent resizeWindowEvent) {
        GL30.glViewport(0, 0, resizeWindowEvent.getNewWidth(), resizeWindowEvent.getNewHeight());
        windowSizeChanged(resizeWindowEvent.getNewWidth(), resizeWindowEvent.getNewHeight());
    }

    @Override
    public void event(ScrollEvent scrollEvent) {

    }

    public void addEventListener(EventListener eventListener) {
        windowEventManager.addEventListener(eventListener);
    }
}
