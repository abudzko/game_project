package com.game.engine;

import com.game.dao.ModelDao;
import com.game.window.Window;

import java.util.Random;

public class Engine implements Runnable {

    protected static final Random RANDOM = new Random();
    private final ModelDao modelDao = new ModelDao();
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
                window.addModel(modelDao.findGameUnit(0));
                window.addModel(modelDao.findGameUnit(1));
                window.addModel(modelDao.findGameUnit(2));
                window.addModel(modelDao.findGameUnit(3));
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

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
