package com.game.window.screen.world;

import com.game.event.window.listener.AbstractWindowEventListener;
import com.game.event.window.mouse.MouseButtonEvent;
import com.game.event.window.resize.ResizeWindowEvent;
import com.game.lwjgl.program.Program;
import com.game.lwjgl.program.RenderObjects;
import com.game.model.DrawableModel;
import com.game.model.GameUnit;
import com.game.utils.log.LogUtil;
import com.game.window.camera.Camera;
import com.game.window.camera.CameraContext;
import com.game.window.camera.CameraEventHandler;
import com.game.window.camera.CameraState;
import com.game.window.camera.world.CameraToWorldConverter;
import com.game.window.camera.world.GroundIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorldScreen extends AbstractWindowEventListener {
    private final Queue<GameUnit> gameUnits = new ConcurrentLinkedQueue<>();
    private final Map<Long, DrawableModel> drawableModels = new ConcurrentHashMap<>();
    private final WorldScreenState worldScreenState;
    private final Program program;
    private final Camera camera;
    private Matrix4f projectionMatrix;
    private boolean isProjectionMatrixChanged = false;

    public WorldScreen(WorldScreenState worldScreenState) {
        this.worldScreenState = worldScreenState;
        this.program = new Program();
        this.camera = createCamera();
        updateMatrices();
    }

    private void updateProjectionMatrix() {
        projectionMatrix = createProjectionMatrix();
        isProjectionMatrixChanged = true;
    }

    private Camera createCamera() {
        var cameraContext = new CameraContext();
        cameraContext.setCameraState(new CameraState());
        var camera = new Camera(cameraContext);
        var cameraEventHandler = new CameraEventHandler(cameraContext);
        subscribeOnWindowEvents(cameraEventHandler);
        return camera;
    }

    private Matrix4f createProjectionMatrix() {
        var aspectRatio = worldScreenState.getWidth() / worldScreenState.getHeight();
        var projectionMatrix = new Matrix4f();
        var cameraState = getCamera().getCameraState();
        projectionMatrix.perspective(
                cameraState.getFov(),
                aspectRatio,
                cameraState.getzNear(),
                cameraState.getzFar()
        );
        return projectionMatrix;
    }

    private RenderObjects createRenderObjects() {
        while (!gameUnits.isEmpty()) {
            var gameUnit = gameUnits.poll();
            var drawableModel = drawableModels.get(gameUnit.getId());
            if (drawableModel == null) {
                drawableModels.put(gameUnit.getId(), getProgram().createDrawableModel(gameUnit));
            } else {
                drawableModel.updateWorldMatrix();
            }
        }

        var renderObjects = new RenderObjects();
        renderObjects.setModels(drawableModels.values());
        if (camera.getCameraState().isCameraViewMatrixChanged()) {
            renderObjects.setCameraViewMatrix(camera.getCameraState().getCameraViewMatrixCopy());
            camera.getCameraState().setCameraViewMatrixChanged(false);
        }
        if (isProjectionMatrixChanged) {
            renderObjects.setProjectionMatrix(projectionMatrix);
            isProjectionMatrixChanged = false;
        }

        return renderObjects;
    }

    public void render() {
        var renderObjects = createRenderObjects();
        getProgram().render(renderObjects);
    }

    public Camera getCamera() {
        if (camera == null) {
            throw new IllegalStateException("Camera is not created");
        }
        return camera;
    }

    public void addGameUnit(GameUnit gameUnit) {
        gameUnits.add(gameUnit);
    }

    // TODO ??
    public Vector3f getWorldCoordinates(MouseButtonEvent mouseButtonEvent) {
        var projectionMatrix = createProjectionMatrix();
        var c = new CameraToWorldConverter(mouseButtonEvent, projectionMatrix, getCamera().getCameraState().getCameraViewMatrixCopy());
        var rayVector = c.rayVector(worldScreenState);
        var cameraState = getCamera().getCameraState();
        var cameraPosition = new Vector3f(cameraState.eyeX, cameraState.eyeY, cameraState.eyeZ);
        var wordCoordinates = new GroundIntersection(cameraPosition).findPoint(rayVector);
        LogUtil.log(String.format("wordCoordinates: X = %s, Y = %s, Z = %s", wordCoordinates.x, wordCoordinates.y, wordCoordinates.z));
        return wordCoordinates;
    }

    public Program getProgram() {
        if (program == null) {
            throw new IllegalStateException("Program is not created");
        }
        return program;
    }

    private void updateMatrices() {
        updateProjectionMatrix();
    }

    @Override
    public void event(ResizeWindowEvent event) {
        super.event(event);
        worldScreenState.setWidth(event.getNewWidth());
        worldScreenState.setHeight(event.getNewHeight());
        updateMatrices();
    }
}
