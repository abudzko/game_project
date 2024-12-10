package com.game.window.camera;

import org.joml.Matrix4f;

public class Camera {

    private final CameraContext cameraContext;

    public Camera(CameraContext cameraContext) {
        this.cameraContext = cameraContext;
    }

    public Matrix4f getLookAtMatrix() {
        return cameraContext.getCameraState().getLookAtMatrixCopy();
    }

    public CameraState getCameraState() {
        return cameraContext.getCameraState();
    }
}
