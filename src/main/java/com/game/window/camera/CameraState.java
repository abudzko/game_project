package com.game.window.camera;

import org.joml.Matrix4f;

public class CameraState {

    public float centerY = 0;
    public float eyeX = 0;
    public float eyeY = 1;
    public float eyeZ = 1;
    public float centerX = 0;
    public float centerZ = 0;
    public float upX = 0;
    public float upY = 1f;
    public float upZ = 0f;
    //+Oz
    public float angle = 0f;
    public boolean isRightMousePressed = false;
    public boolean isRightMouseReleased = true;
    public float cursorPositionX;
    public float cursorPositionY;
    public float previousCursorPositionX;
    public float previousCursorPositionY;
    private float fov = (float) Math.toRadians(50f);
    private float zNear = 0.05f;
    private float zFar = 30.f;
    private float moveStep = 0.1f;
    private Matrix4f cameraViewMatrix;
    private boolean cameraViewMatrixChanged = false;

    public boolean isCameraViewMatrixChanged() {
        return cameraViewMatrixChanged;
    }

    public void setCameraViewMatrixChanged(boolean cameraViewMatrixChanged) {
        this.cameraViewMatrixChanged = cameraViewMatrixChanged;
    }

    public float getMoveStep() {
        return moveStep;
    }

    public float getFov() {
        return fov;
    }

    public float getzNear() {
        return zNear;
    }

    public float getzFar() {
        return zFar;
    }

    public Matrix4f getCameraViewMatrixCopy() {
        return new Matrix4f(cameraViewMatrix);
    }

    public void setCameraViewMatrix(Matrix4f cameraViewMatrix) {
        this.cameraViewMatrix = cameraViewMatrix;
        this.cameraViewMatrixChanged = true;
    }
}
