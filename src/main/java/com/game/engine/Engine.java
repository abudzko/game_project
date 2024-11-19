package com.game.engine;

import com.game.dao.GameUnitDao;
import com.game.model.GameUnit;
import com.game.window.Window;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class Engine implements Runnable {

    protected static final Random RANDOM = new Random();
    private final GameUnitDao gameUnitDao = new GameUnitDao();
    private final Window window;

    private volatile boolean isRunning = false;

    public Engine(Window window) {
        this.window = window;
    }

    @Override
    public void run() {
        isRunning = true;
        boolean set = false;
        while (isRunning) {
            if (!set) {
                set = true;
                gameUnitDao.getUnits().forEach(window::addGameUnit);
                addRandomUnits();
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
        for (int i = 0; i < 100; i++) {
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

    private float resolvePosition(float pos) {
        return pos >= 10 || pos <= -10 ? 0 : pos + (RANDOM.nextBoolean() ? 1 : -1) * 0.05f;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }


}
