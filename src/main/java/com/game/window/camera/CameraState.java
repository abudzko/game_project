package com.game.window.camera;

import com.game.utils.log.LogUtil;
import org.joml.Matrix4f;

public class CameraState {

    public final float centerY = 0;

    public Matrix4f getLookAtMatrixCopy() {
        return new Matrix4f(lookAtMatrix);
    }

    public void setLookAtMatrix(Matrix4f lookAtMatrix) {
        LogUtil.log("Changed:" + lookAtMatrix.toString());
        this.lookAtMatrix = lookAtMatrix;
    }

    private Matrix4f lookAtMatrix;
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
}