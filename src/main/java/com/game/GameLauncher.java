package com.game;

import org.lwjgl.Version;

import static com.game.utils.log.LogUtil.log;

/**
 * Launch new Game instance
 */
public class GameLauncher {
    public static void main(String[] args) {
        new GameLauncher().launch();
    }

    public void launch() {
        log(String.format("Starting LWJGL: %s version.", Version.getVersion()));
        new Game().start();
    }
}
