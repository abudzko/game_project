package com.game.shader;

import com.game.dao.TextureDao;
import com.game.dao.VerticesDao;
import com.game.model.Model;
import com.game.utils.math.Matrix4f;
import com.game.window.DrawableModel;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.util.concurrent.ConcurrentHashMap;

public class Program {
    protected static final int POINT_PER_VERTEX_3D = 3;
    protected static final String PROJECTION_MATRIX_NAME = "projectionMatrix";
    protected static final String CAMERA_VIEW_MATRIX_NAME = "cameraViewMatrix";
    protected static final String WORLD_MATRIX_NAME = "worldMatrix";
    protected static final String LOCATION_ATTRIBUTE_NAME = "locationAttribute";
    protected static final String TEXTURE_ATTRIBUTE_NAME = "textureAttribute";
    protected static final String SHADER_PATH = "src/main/resources/shaders/";
    private final Shader vertexShader;
    private final Shader fragmentShader;
    private final ConcurrentHashMap<String, Integer> uniformCache = new ConcurrentHashMap<>();
    private final long windowId;
    private final VerticesDao verticesDao = new VerticesDao();
    private final TextureDao textureDao = new TextureDao();
    private Matrix4f cameraViewMatrix;
    private boolean cameraViewMatrixChanged = false;
    private Matrix4f projectionMatrix;
    private boolean projectionMatrixChanged = false;
    private int programId;

    public Program(long windowId) {
        this.windowId = windowId;
        this.vertexShader = new Shader(SHADER_PATH + "v.vert", GL20.GL_VERTEX_SHADER);
        this.fragmentShader = new Shader(SHADER_PATH + "f.frag", GL20.GL_FRAGMENT_SHADER);
    }

    public void linkProgram() {
        programId = GL20.glCreateProgram();

        GL20.glAttachShader(programId, vertexShader.getId());
        GL20.glAttachShader(programId, fragmentShader.getId());

        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);

        releaseResources();
    }

    public void render(Iterable<DrawableModel> models) {
        GLFW.glfwSwapBuffers(windowId);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        enable();
        var locationAttribute = GL20.glGetAttribLocation(getProgramId(), LOCATION_ATTRIBUTE_NAME);
        var textureAttribute = GL20.glGetAttribLocation(getProgramId(), TEXTURE_ATTRIBUTE_NAME);

        for (var drawableModel : models) {
            setUniformMatrix4f(WORLD_MATRIX_NAME, drawableModel.getWorldMatrix());
            // Bind to the VAO
            GL30.glBindVertexArray(drawableModel.getVaoId());

            // Enable Texture
            GL20.glEnableVertexAttribArray(textureAttribute);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, drawableModel.getTextureId());

            // Draw the vertices
            GL20.glEnableVertexAttribArray(locationAttribute);
            int verticesCount = drawableModel.getVertices().limit() / POINT_PER_VERTEX_3D;
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, verticesCount);

            // Restore state
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL20.glDisableVertexAttribArray(textureAttribute);
            GL20.glDisableVertexAttribArray(locationAttribute);
            GL30.glBindVertexArray(0);
        }

        if (cameraViewMatrixChanged) {
            setUniformMatrix4f(CAMERA_VIEW_MATRIX_NAME, cameraViewMatrix);
            cameraViewMatrixChanged = false;
        }
        if (projectionMatrixChanged) {
            setUniformMatrix4f(PROJECTION_MATRIX_NAME, projectionMatrix);
            projectionMatrixChanged = false;
        }
        disable();
    }

    public DrawableModel createDrawableModel(Model model) {
        int id = model.getId();

        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Vertices
        var vertices = verticesDao.getVertices(id);
        int verticesVboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticesVboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        var locationAttribute = GL20.glGetAttribLocation(getProgramId(), LOCATION_ATTRIBUTE_NAME);
        GL20.glVertexAttribPointer(locationAttribute, POINT_PER_VERTEX_3D, GL11.GL_FLOAT, false, 0, 0);

        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        MemoryUtil.memFree(vertices);

        // Textures
        var texture = textureDao.getTexture(id);
        int textureVboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureVboId);
        var textureVertices = texture.getTextureVertices();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureVertices, GL15.GL_STATIC_DRAW);
        var textureAttribute = GL20.glGetAttribLocation(getProgramId(), TEXTURE_ATTRIBUTE_NAME);
        GL20.glVertexAttribPointer(textureAttribute, 2, GL11.GL_FLOAT, false, 0, 0);
        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Unbind the VAO
        GL30.glBindVertexArray(0);
        MemoryUtil.memFree(textureVertices);
        return new DrawableModel(model, vaoId, vertices, texture);
    }

    private void releaseResources() {
        vertexShader.deleteShader();
        fragmentShader.deleteShader();
    }

    public void cameraViewMatrixChanged(Matrix4f cameraViewMatrix) {
        this.cameraViewMatrix = cameraViewMatrix;
        this.cameraViewMatrixChanged = true;
    }

    public void projectionMatrixChanged(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
        this.projectionMatrixChanged = true;
    }

    private void setUniformMatrix4f(String name, Matrix4f matrix4f) {
        GL20.glUniformMatrix4fv(
                getUniformIdBy(name),
                false,
                matrix4f.toFloatBuffer()
        );
    }

    private int getUniformIdBy(String uniformName) {
        return uniformCache.computeIfAbsent(uniformName, name -> {
            var uniformId = GL20.glGetUniformLocation(programId, uniformName);
            if (uniformId == -1) {
                throw new IllegalArgumentException(String.format(
                        "Could not find uniform location by name: %s",
                        uniformName
                ));
            }
            return uniformId;
        });
    }

    private int getProgramId() {
        return programId;
    }

    private void enable() {
        GL20.glUseProgram(programId);
    }

    private void disable() {
        GL20.glUseProgram(0);
    }
}
