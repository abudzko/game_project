package com.game.engine;

import com.game.dao.ModelDao;
import com.game.model.Model;
import com.game.window.Monitor;
import com.game.window.Window;

import static com.game.window.WindowFactory.MAIN_WINDOW_NAME;

public class Engine implements Runnable {

    private final ModelDao modelDao = new ModelDao();

    private volatile boolean isRunning = false;
    private Monitor monitor;

    public Engine() {
    }

    @Override
    public void run() {
        isRunning = true;
        boolean set = false;
        while (isRunning) {
            Window mainWindow = monitor.getWindow(MAIN_WINDOW_NAME);
            if (mainWindow != null) {//TODO fix
                if (!set) {
                    set = true;
                    mainWindow.addModel(modelDao.getModel(0));
                    mainWindow.addModel(modelDao.getModel(1));
                    Model m = modelDao.getModel(1);
                    m.getPosition().set(0, 0, 0);
                    mainWindow.addModel(m);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mainWindow.shouldBeClosed()) {
                    break;
                }
            }
        }
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }
}
