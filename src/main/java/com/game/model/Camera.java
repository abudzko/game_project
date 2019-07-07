package com.game.model;

import com.game.utils.math.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f lookAtMatrix;

    private float x = 0;

    private float centerZ = 0;
    private float eyeZ = 1;
    public Camera() {
        lookAtMatrix = new Matrix4f();
        lookAtMatrix.lookAt(
                new Vector3f(0f, 1f, 1f),
                new Vector3f(0f, 0f, 0f),
                new Vector3f(0f, 1f, 0f)
        );
    }

    public void moveY(float delta) {
        this.centerZ += delta;
        this.eyeZ += delta;
        Matrix4f m = new Matrix4f();
        m.lookAt(
                new Vector3f(x, 1f, eyeZ),
                new Vector3f(x, 0f, centerZ),
                new Vector3f(0f, 1f, 0f)
        );
        this.lookAtMatrix = m;
    }

    public void moveX(float delta) {
        this.x += delta;
        Matrix4f m = new Matrix4f();
        m.lookAt(
                new Vector3f(x, 1f, eyeZ),
                new Vector3f(x, 0f, centerZ),
                new Vector3f(0f, 1f, 0f)
        );
        this.lookAtMatrix = m;
    }

    public Matrix4f getLookAtMatrix() {
        return lookAtMatrix;
    }


}
