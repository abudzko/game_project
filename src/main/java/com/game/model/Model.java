package com.game.model;

import org.joml.Vector3f;

/**
 * Represents model of 3d object:
 * - vertices
 * - positions
 * - rotation
 * - scale
 */
public class Model {
    private final int id;
    private final Vector3f position;
    /**
     * An angles measured in degrees
     */
    private final Vector3f rotation;
    private final float scale;

    public Model(
            int id,
            Vector3f position,
            Vector3f rotation,
            float scale
    ) {
        this.id = id;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
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

    public int getId() {
        return id;
    }
}
