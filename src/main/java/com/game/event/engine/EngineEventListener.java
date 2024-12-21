package com.game.event.engine;

public interface EngineEventListener {
    default void event(EngineEvent event) {
    }
}
