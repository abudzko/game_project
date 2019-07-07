package com.game.window;

import com.game.model.Model;
import com.game.shader.Texture;
import com.game.utils.math.Matrix4f;

import java.nio.FloatBuffer;

public class DrawableModel {
    private int vaoId;

    private FloatBuffer vertices;

    private Texture texture;

    private volatile Matrix4f worldMatrix;

    DrawableModel(
            Model model,
            int vaoId,
            FloatBuffer vertices,
            Texture texture
    ) {
        this.vaoId = vaoId;
        this.vertices = vertices;
        this.texture = texture;
        updateWorldMatrix(model);
    }

    public void updateWorldMatrix(Model model) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.translate(model.getPosition())
                .rotateX((float) Math.toRadians(model.getRotation().x))
                .rotateY((float) Math.toRadians(model.getRotation().y))
                .rotateZ((float) Math.toRadians(model.getRotation().z))
                .scale(model.getScale());
        this.worldMatrix = matrix4f;
    }

    public int getVaoId() {
        return vaoId;
    }

    public FloatBuffer getVertices() {
        return vertices;
    }

    public int getTextureId() {
        return texture.getTextureId();
    }

    public Matrix4f getWorldMatrix() {
        return worldMatrix;
    }
}
