package com.game;

import com.game.engine.Engine;
import com.game.window.Window;
import com.game.window.WindowConfig;

public class Game {

    private Window window;

    private Engine engine;

    public Game() {
        var windowConfig = new WindowConfig();
        window = new Window(windowConfig);
        engine = new Engine(window);
    }

    public static void main(String[] args) {
        new Game();
    }

    public void start() {
        try {
            window.start();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
        new Thread(engine).start();
    }
}
