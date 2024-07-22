package com.game.window;

import com.game.utils.log.LogUtil;
import com.game.utils.math.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f lookAtMatrix;

    private float eyeX = 0;
    private float eyeY = 1;
    private float eyeZ = 1;

    private float centerX = 0;
    private final float centerY = 0;
    private float centerZ = 0;

    private final float upX = 0;
    private final float upY = 1;
    private final float upZ = 0;

    public Camera() {
        look();
    }

    public void moveX(float delta) {
        this.eyeX += delta;
        this.centerX += delta;
        look();
    }

    public void moveY(float delta) {
        float newValue = this.eyeY + delta;
        if (newValue > 0.01) {
            LogUtil.log(String.format("Move camera from %s to %s eyeY position", this.eyeY, newValue));
            this.eyeY = newValue;
            look();
        } else {
            LogUtil.log(String.format("Can't move camera from %s to %s eyeY position", this.eyeY, newValue));
        }
    }

    public void moveZ(float delta) {
        this.centerZ += delta;
        this.eyeZ += delta;
        LogUtil.log(String.format("Move camera to %s eyeZ position", this.eyeZ));
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
