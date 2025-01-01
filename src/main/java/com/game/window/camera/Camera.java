package com.game.window.camera;

public class Camera {
    private CameraState cameraState;

    public Camera(CameraState cameraState) {
        this.cameraState = cameraState;
    }

    public CameraState getCameraState() {
        return cameraState;
    }
}
