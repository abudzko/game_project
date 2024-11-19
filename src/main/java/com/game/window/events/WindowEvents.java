package com.game.window.events;

import com.game.dao.GameUnitDao;
import com.game.utils.BufferUtils;
import com.game.utils.log.LogUtil;
import com.game.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * TODO create event listener interfaces and add listeners into this class
 */
public class WindowEvents {
    private final Window window;

    private final GameUnitDao gameUnitDao = new GameUnitDao();
    private final float moveStep = 0.05f;

    public WindowEvents(Window window) {
        this.window = window;
    }

    public void attachEvents() {
        GLFW.glfwSetKeyCallback(window.getWindowId(), new GLFWKeyCallback() {
            @Override
            public void invoke(long windowId, int key, int scanCode, int action, int mods) {
                var keyEvent = EventFactory.buildKeyEvent(key, scanCode, action, mods);
                processKeyEvent(keyEvent);
            }
        });

        var cursorX = BufferUtils.createByteBuffer(new byte[8]);
        var cursorY = BufferUtils.createByteBuffer(new byte[8]);
        GLFW.glfwSetMouseButtonCallback(window.getWindowId(), new GLFWMouseButtonCallback() {

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

        GLFW.glfwSetScrollCallback(window.getWindowId(), new GLFWScrollCallback() {
            @Override
            public void invoke(long windowId, double offsetX, double offsetY) {
                var scrollEvent = new ScrollEvent(offsetX, offsetY);
                processScrollEvent(scrollEvent);
            }
        });
        GLFW.glfwSetCursorPosCallback(window.getWindowId(), new GLFWCursorPosCallback() {
            @Override
            public void invoke(long windowId, double xpos, double ypos) {

            }
        });

        GLFW.glfwSetCursorEnterCallback(window.getWindowId(), new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {

            }
        });

        GLFW.glfwSetWindowSizeCallback(window.getWindowId(), new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long windowId, int newWidth, int newHeight) {
                glViewport(0, 0, newWidth, newHeight);
                var windowResizeEvent = EventFactory.buildWindowResizeEvent(newWidth, newHeight);
                processWindowResizeEvent(windowResizeEvent);
            }
        });
    }

    public void processKeyEvent(KeyEvent keyEvent) {
        float step = window.getWindowConfig().getCameraMoveStep();

        switch (keyEvent.getAction()) {
            case GLFW.GLFW_PRESS:
                switch (keyEvent.getKey()) {
                    case GLFW.GLFW_KEY_UP:
                        window.moveCameraZ(-step);
                        break;
                    case GLFW.GLFW_KEY_DOWN:
                        window.moveCameraZ(step);
                        break;
                    case GLFW.GLFW_KEY_RIGHT:
                        window.moveCameraX(step);
                        break;
                    case GLFW.GLFW_KEY_LEFT:
                        window.moveCameraX(-step);
                        break;
                    case GLFW.GLFW_KEY_W:
                        moveZ(-moveStep);
                        break;
                    case GLFW.GLFW_KEY_S:
                        moveZ(moveStep);
                        break;
                    case GLFW.GLFW_KEY_A:
                        moveX(-moveStep);
                        break;
                    case GLFW.GLFW_KEY_D:
                        moveX(moveStep);
                        break;
                    case GLFW.GLFW_KEY_ESCAPE:
                        GLFW.glfwSetWindowShouldClose(window.getWindowId(), true);
                        LogUtil.log(String.format("Close window %s", window.getWindowId()));
                        break;
                    default:
                        LogUtil.log(String.format("Pressed %s", keyEvent.getKey()));
                        break;
                }
                break;
            case GLFW.GLFW_REPEAT:
                switch (keyEvent.getKey()) {
                    case GLFW.GLFW_KEY_UP:
                        window.moveCameraZ(-step);
                        break;
                    case GLFW.GLFW_KEY_DOWN:
                        window.moveCameraZ(step);
                        break;
                    case GLFW.GLFW_KEY_RIGHT:
                        window.moveCameraX(step);
                        break;
                    case GLFW.GLFW_KEY_LEFT:
                        window.moveCameraX(-step);
                        break;
                    case GLFW.GLFW_KEY_W:
                        moveZ(-moveStep);
                        break;
                    case GLFW.GLFW_KEY_S:
                        moveZ(moveStep);
                        break;
                    case GLFW.GLFW_KEY_A:
                        moveX(-moveStep);
                        break;
                    case GLFW.GLFW_KEY_D:
                        moveX(moveStep);
                        break;
                    default:
                        break;
                }
                break;
            case GLFW.GLFW_RELEASE:
                break;
            default:
                break;
        }

    }

    public void processScrollEvent(ScrollEvent scrollEvent) {
        window.moveCameraY((int) scrollEvent.getOffsetY() * (-window.getWindowConfig().getCameraMoveStep()));
    }

    public void processWindowResizeEvent(WindowResizeEvent windowResizeEvent) {
        window.windowSizeChanged(windowResizeEvent.getNewWidth(), windowResizeEvent.getNewHeight());
    }

    public void processMouseEvent(MouseEvent mouseEvent) {

    }

    /**
     * TODO This logic should be in event listeners
     */
    private void moveX(float stepX) {
        var gameUnit = gameUnitDao.getMainUnit();
        System.out.println("X: " + gameUnit.getPosition().x);
        gameUnit.getPosition().x += stepX;
        //TODO just redraw model
        window.addGameUnit(gameUnit);
    }

    /**
     * TODO This logic should be in event listeners
     */
    private void moveZ(float stepZ) {
        var gameUnit = gameUnitDao.getMainUnit();
        System.out.println("Z: " + gameUnit.getPosition().z);
        gameUnit.getPosition().z += stepZ;
        //TODO just redraw model
        window.addGameUnit(gameUnit);
    }

}
