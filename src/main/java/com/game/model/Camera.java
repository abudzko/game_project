package com.game.model;

import com.game.utils.math.Matrix4f;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f lookAtMatrix;

    private float eyeX = 0;
    private float eyeY = 1;
    private float eyeZ = 1;

    private float centerX = 0;
    private float centerY = 0;
    private float centerZ = 0;

    private float upX = 0;
    private float upY = 1;
    private float upZ = 0;

    public Camera() {
        look();
    }

    public void moveX(float delta) {
        this.eyeX += delta;
        this.centerX += delta;
        look();
    }

    public void moveY(float delta) {
        this.centerZ += delta;
        this.eyeZ += delta;
        look();
    }

    public void moveZ(float delta) {
        this.eyeY += delta;
        look();
    }

    private void look() {
        Matrix4f m = new Matrix4f();
        m.lookAt(eye(), center(), up());
        lookAtMatrix = m;
    }

    private Vector3f eye() {
        return new Vector3f(eyeX, eyeY, eyeZ);
    }

    private Vector3f center() {
        return new Vector3f(centerX, centerY, centerZ);
    }

    private Vector3f up() {
        return new Vector3f(upX, upY, upZ);
    }

    public Matrix4f getLookAtMatrix() {
        return lookAtMatrix;
    }
}
