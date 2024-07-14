package com.game.engine;

import com.game.dao.ModelDao;
import com.game.window.Window;

public class Engine implements Runnable {

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
                window.addModel(modelDao.getModel(0));
                window.addModel(modelDao.getModel(1));
                window.addModel(modelDao.getModel(2));
            }
            if (window.shouldBeClosed()) {
                break;
            }
        }
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
