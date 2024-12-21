package com.game.engine;

import com.game.dao.GameUnitDao;
import com.game.event.window.key.KeyEvent;
import com.game.event.window.listener.KeyEventListener;
import com.game.event.window.listener.MouseButtonEventListener;
import com.game.event.window.mouse.MouseButton;
import com.game.event.window.mouse.MouseButtonAction;
import com.game.event.window.mouse.MouseButtonEvent;
import com.game.model.GameUnit;
import com.game.utils.log.LogUtil;
import com.game.window.Window;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

/**
 * TODO screen??
 */
public class Engine implements Runnable, KeyEventListener, MouseButtonEventListener {

    protected static final Random RANDOM = new Random();
    private final GameUnitDao gameUnitDao = new GameUnitDao();
    private final Window window;
    private final float moveStep = 0.01f;
    private volatile boolean isRunning = false;
    private GameUnit selectedUnit;

    public Engine(Window window) {
        this.window = window;
    }

    private static float rotation(float value) {
        return value + 5 * RANDOM.nextFloat();
    }

    @Override
    public void run() {
        window.registerWindowEventListener(this);
        isRunning = true;
        boolean set = false;
        //TODO why loop?
        while (isRunning) {
            if (!set) {
                set = true;
                var mainUnit = gameUnitDao.getMainUnit();
                selectedUnit = mainUnit;
                window.addGameUnit(mainUnit);
                animate(mainUnit);
                gameUnitDao.getUnits().forEach(window::addGameUnit);
//                addRandomUnits();
            }
            if (window.shouldBeClosed()) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private void addRandomUnits() {
        var units = new ArrayList<GameUnit>();
        for (int i = 0; i < 10; i++) {
            GameUnit gameUnit = gameUnitDao.createGameUnit();
            units.add(gameUnit);
            window.addGameUnit(gameUnit);
        }
        Thread thread = new Thread(() -> {
            while (isRunning) {
                units.forEach(new Consumer<GameUnit>() {
                    @Override
                    public void accept(GameUnit gameUnit) {
                        gameUnit.getPosition().x = resolvePosition(gameUnit.getPosition().x);
                        gameUnit.getPosition().z = resolvePosition(gameUnit.getPosition().z);
                        gameUnit.getRotation().x = rotation(gameUnit.getRotation().x);
                        window.addGameUnit(gameUnit);
                    }
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void animate(GameUnit gameUnit) {
        Thread thread = new Thread(() -> {
            while (isRunning) {
                try {
                    gameUnit.getRotation().y = rotation(gameUnit.getRotation().y);
                    window.addGameUnit(gameUnit);
                    Thread.sleep(100);
                } catch (Exception e) {
                    LogUtil.logError("animate failed", e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private float resolvePosition(float pos) {
        return pos >= 10 || pos <= -10 ? 0 : pos + (RANDOM.nextBoolean() ? 1 : -1) * 0.01f;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void event(KeyEvent keyEvent) {
        handleKeyEventForSelectedGameUnit(keyEvent);
    }

    private void handleKeyEventForSelectedGameUnit(KeyEvent keyEvent) {
        switch (keyEvent.getKeyActionType()) {
            case PRESSED:
                switch (keyEvent.getKey()) {
                    case KEY_W:
                        moveZ(-moveStep);
                        break;
                    case KEY_S:
                        moveZ(moveStep);
                        break;
                    case KEY_A:
                        moveX(-moveStep);
                        break;
                    case KEY_D:
                        moveX(moveStep);
                        break;
                    case KEY_ESCAPE:
                        GLFW.glfwSetWindowShouldClose(window.getWindowId(), true);
                        LogUtil.log(String.format("Close window %s", window.getWindowId()));
                        break;
                    default:
                        LogUtil.log(String.format("Pressed %s", keyEvent.getKeyDeprecated()));
                        break;
                }
                break;
            case REPEAT:
                switch (keyEvent.getKey()) {
                    case KEY_W:
                        moveZ(-moveStep);
                        break;
                    case KEY_S:
                        moveZ(moveStep);
                        break;
                    case KEY_A:
                        moveX(-moveStep);
                        break;
                    case KEY_D:
                        moveX(moveStep);
                        break;
                    default:
                        break;
                }
                break;
            case RELEASED:
                break;
            default:
                break;
        }
    }

    private void moveX(float stepX) {
        var localSelectedUnit = selectedUnit;
        if (localSelectedUnit != null) {
            System.out.println("X: " + localSelectedUnit.getPosition().x);
            localSelectedUnit.getPosition().x += stepX;
            //TODO just redraw model
            window.addGameUnit(localSelectedUnit);
        }
    }

    private void moveZ(float stepZ) {
        var localSelectedUnit = selectedUnit;
        if (localSelectedUnit != null) {
            System.out.println("Z: " + localSelectedUnit.getPosition().z);
            localSelectedUnit.getPosition().z += stepZ;
            //TODO just redraw model
            window.addGameUnit(localSelectedUnit);
        }
    }

    @Override
    public void event(MouseButtonEvent mouseButtonEvent) {
        if (MouseButtonAction.PRESSED.equals(mouseButtonEvent.getAction())
                && MouseButton.LEFT.equals(mouseButtonEvent.getButton())) {
            Vector3f worldCoordinates = window.getWorldCoordinates(mouseButtonEvent);
            window.addGameUnit(GameUnitDao.createCudeGameUnit(worldCoordinates));
        }
    }
}
