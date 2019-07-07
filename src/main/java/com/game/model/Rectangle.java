package com.game.model;

import com.game.utils.math.Matrix4f;
import org.joml.Vector3f;


public class Rectangle/* implements Drawable */{

    private static final int POINT_PER_VERTEX_3D = 3;

    private int vaoId;

    private final Model model;

    private int attributeLocation;
    private int attributeColor;
    private int attributeTexture;
    private volatile Matrix4f worldMatrix;

    public Rectangle(Model model, int attributeLocation, int attributeColor, int attributeTexture) {
        this.model = model;

        this.attributeLocation = attributeLocation;
        this.attributeColor = attributeColor;
        this.attributeTexture = attributeTexture;
    }

//    public void init() {
//        updateWorldMatrix();
//
//        vaoId = GL30.glGenVertexArrays();
//        GL30.glBindVertexArray(vaoId);
//
//        int vboId = GL15.glGenBuffers();
//        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
//        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, model.getVertices(), GL15.GL_STATIC_DRAW);
//
//        GL20.glVertexAttribPointer(attributeLocation, POINT_PER_VERTEX_3D, GL11.GL_FLOAT, false, 0, 0);
//
//        // Unbind the VBO
//        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//
//        // colors
////        vboId = GL15.glGenBuffers();
////        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
////        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, model.getColors(), GL15.GL_STATIC_DRAW);
////
////        GL20.glVertexAttribPointer(attributeColor, 4, GL11.GL_FLOAT, false, 0, 0);
////        // Unbind the VBO
////        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//
//        // textures
//        vboId = GL15.glGenBuffers();
//        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
//        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, model.getTextureVertices(), GL15.GL_STATIC_DRAW);
//
//        GL20.glVertexAttribPointer(attributeTexture, 2, GL11.GL_FLOAT, false, 0, 0);
//        // Unbind the VBO
//        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//
//
//        // Unbind the VAO
//        GL30.glBindVertexArray(0);
//    }

    public void updateWorldMatrix() {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.translate(model.getPosition())
                .rotateX((float) Math.toRadians(model.getRotation().x))
                .rotateY((float) Math.toRadians(model.getRotation().y))
                .rotateZ((float) Math.toRadians(model.getRotation().z))
                .scale(model.getScale());
        this.worldMatrix = matrix4f;
    }

    public void setRotation(Vector3f rotation) {
        model.getRotation().set(rotation.x, rotation.y, rotation.z);
        updateWorldMatrix();
    }

    public void setPosition(Vector3f position) {
        model.getPosition().set(position.x, position.y, position.z);
        updateWorldMatrix();
    }

//    @Override
//    public void draw() {
//        // Bind to the VAO
//        GL30.glBindVertexArray(vaoId);
//        GL20.glEnableVertexAttribArray(attributeLocation);
//        GL20.glEnableVertexAttribArray(attributeColor);
//        GL20.glEnableVertexAttribArray(attributeTexture);
//
//        GL13.glActiveTexture(GL13.GL_TEXTURE0);
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTextureId());
//        // Draw the vertices
//        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertices().limit() / POINT_PER_VERTEX_3D);
//
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
//        // Restore state
//        GL20.glDisableVertexAttribArray(attributeLocation);
//        GL20.glDisableVertexAttribArray(attributeColor);
//        GL20.glDisableVertexAttribArray(attributeTexture);
//        GL30.glBindVertexArray(0);
//    }
//
//    public Matrix4f getWorldMatrix() {
//        return worldMatrix;
//    }
//
//    public Model getModel() {
//        return model;
//    }
}
