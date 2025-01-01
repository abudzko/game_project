package com.game.model;

import com.game.event.window.key.KeyEvent;
import com.game.event.window.listener.KeyEventListener;
import org.joml.Vector3f;

/**
 * Represents the game unit:
 * - positions
 * - rotation
 * - scale
 */
public class GameUnit {
    private final long id;
    private final Vector3f position;
    /**
     * An angles measured in degrees
     */
    private final Vector3f rotation;
    private final float scale;

    private final Model model;

    public GameUnit(
            long id,
            Vector3f position,
            Vector3f rotation,
            float scale,
            Model model
    ) {
        this.id = id;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public long getId() {
        return id;
    }

    public Model getModel() {
        return model;
    }
}
