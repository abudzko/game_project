package com.game.window.camera;

public class Camera {

    private final CameraContext cameraContext;

    public Camera(CameraContext cameraContext) {
        this.cameraContext = cameraContext;
    }

    public CameraState getCameraState() {
        return cameraContext.getCameraState();
    }
}
