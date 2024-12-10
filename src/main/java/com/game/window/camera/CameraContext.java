package com.game.window.camera;

import com.game.lwjgl.program.Program;

public class CameraContext {
    private float moveStep;
    private Program program;
    private CameraState cameraState;

    public CameraState getCameraState() {
        return cameraState;
    }

    public void setCameraState(CameraState cameraState) {
        this.cameraState = cameraState;
    }

    public float getMoveStep() {
        return moveStep;
    }

    public void setMoveStep(float moveStep) {
        this.moveStep = moveStep;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
}
