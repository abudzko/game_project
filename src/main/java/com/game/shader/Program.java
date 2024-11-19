package com.game.shader;

import com.game.model.GameUnit;
import com.game.utils.BufferUtils;
import com.game.utils.math.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL30.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL30.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL30.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL30.GL_FLOAT;
import static org.lwjgl.opengl.GL30.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL30.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_TRIANGLES;
import static org.lwjgl.opengl.GL30.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL30.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glAttachShader;
import static org.lwjgl.opengl.GL30.glBindBuffer;
import static org.lwjgl.opengl.GL30.glBindTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glBufferData;
import static org.lwjgl.opengl.GL30.glClear;
import static org.lwjgl.opengl.GL30.glCreateProgram;
import static org.lwjgl.opengl.GL30.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glDrawArrays;
import static org.lwjgl.opengl.GL30.glDrawElements;
import static org.lwjgl.opengl.GL30.glEnable;
import static org.lwjgl.opengl.GL30.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glGetAttribLocation;
import static org.lwjgl.opengl.GL30.glGetUniformLocation;
import static org.lwjgl.opengl.GL30.glLinkProgram;
import static org.lwjgl.opengl.GL30.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glUseProgram;
import static org.lwjgl.opengl.GL30.glValidateProgram;
import static org.lwjgl.opengl.GL30.glVertexAttribPointer;

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
    private Matrix4f cameraViewMatrix;
    private boolean cameraViewMatrixChanged = false;
    private Matrix4f projectionMatrix;
    private boolean projectionMatrixChanged = false;
    private int programId;

    public Program(long windowId) {
        this.windowId = windowId;
        this.vertexShader = new Shader(SHADER_PATH + "v.vert", GL_VERTEX_SHADER);
        this.fragmentShader = new Shader(SHADER_PATH + "f.frag", GL_FRAGMENT_SHADER);
    }

    public void linkProgram() {
        programId = glCreateProgram();

        glAttachShader(programId, vertexShader.getId());
        glAttachShader(programId, fragmentShader.getId());

        glLinkProgram(programId);
        glValidateProgram(programId);

        releaseResources();
    }

    public void render(Iterable<DrawableModel> models) {
        GLFW.glfwSwapBuffers(windowId);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);

        enable();
        var locationAttribute = glGetAttribLocation(getProgramId(), LOCATION_ATTRIBUTE_NAME);
        var textureAttribute = glGetAttribLocation(getProgramId(), TEXTURE_ATTRIBUTE_NAME);

        for (var drawableModel : models) {
            setUniformMatrix4f(WORLD_MATRIX_NAME, drawableModel.getWorldMatrix());
            // Bind to the VAO
            glBindVertexArray(drawableModel.getVaoId());

            // Enable Texture
            glEnableVertexAttribArray(textureAttribute);
            glBindTexture(GL_TEXTURE_2D, drawableModel.getTextureId());

            // Draw the vertices
            glEnableVertexAttribArray(locationAttribute);
            int verticesCount = drawableModel.getVertices().limit() / POINT_PER_VERTEX_3D;
            glDrawArrays(GL_TRIANGLES, 0, verticesCount);

            // Clean resources
            glBindTexture(GL_TEXTURE_2D, 0);
            glDisableVertexAttribArray(textureAttribute);
            glDisableVertexAttribArray(locationAttribute);
            glBindVertexArray(0);
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

    public void renderV2(Iterable<DrawableModel> models) {
        GLFW.glfwSwapBuffers(windowId);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);

        enable();
        var locationAttribute = glGetAttribLocation(getProgramId(), LOCATION_ATTRIBUTE_NAME);
        var textureAttribute = glGetAttribLocation(getProgramId(), TEXTURE_ATTRIBUTE_NAME);

        for (var drawableModel : models) {
            setUniformMatrix4f(WORLD_MATRIX_NAME, drawableModel.getWorldMatrix());

            // Enable Texture
            glEnableVertexAttribArray(textureAttribute);
            glBindTexture(GL_TEXTURE_2D, drawableModel.getTextureId());

            // Draw the vertices
            glEnableVertexAttribArray(locationAttribute);
            int verticesCount = drawableModel.getVertices().limit() / POINT_PER_VERTEX_3D;
            glDrawElements(GL_TRIANGLES, verticesCount, GL_UNSIGNED_INT, 0);

            // Clean resources
            glBindTexture(GL_TEXTURE_2D, 0);
            glDisableVertexAttribArray(textureAttribute);
            glDisableVertexAttribArray(locationAttribute);
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

    public DrawableModel createDrawableModel(GameUnit gameUnit) {
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Vertices
        var vertices = gameUnit.getModel().triangleVertices();
        int verticesVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, verticesVboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        var locationAttribute = glGetAttribLocation(getProgramId(), LOCATION_ATTRIBUTE_NAME);
        glVertexAttribPointer(locationAttribute, POINT_PER_VERTEX_3D, GL_FLOAT, false, 0, 0);

        // Unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        MemoryUtil.memFree(vertices);

        // Textures
        int textureVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureVboId);
        var textureVertices = BufferUtils.createFloatBuffer(gameUnit.getModel().modelTexture().textureVertices());
        glBufferData(GL_ARRAY_BUFFER, textureVertices, GL_STATIC_DRAW);
        var textureAttribute = glGetAttribLocation(getProgramId(), TEXTURE_ATTRIBUTE_NAME);
        glVertexAttribPointer(textureAttribute, 2, GL_FLOAT, false, 0, 0);
        // Unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        MemoryUtil.memFree(textureVertices);

        // Unbind the VAO
        glBindVertexArray(0);
        return new DrawableModel(gameUnit, vaoId, vertices);
    }

    public DrawableModel createDrawableModelV2(GameUnit gameUnit) {
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Vertices
        var vertices = gameUnit.getModel().triangleVertices();
        int verticesVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, verticesVboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        var locationAttribute = glGetAttribLocation(getProgramId(), LOCATION_ATTRIBUTE_NAME);
        glVertexAttribPointer(locationAttribute, POINT_PER_VERTEX_3D, GL_FLOAT, false, 0, 0);
        // Unbind vertexes VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        MemoryUtil.memFree(vertices);

        // Indexes
        int indexVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, indexVboId);
        var indexes = BufferUtils.createIntBuffer(new int[0]);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexes, GL_STATIC_DRAW);
        // Unbind the indexes VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        MemoryUtil.memFree(indexes);

        // Textures
        var texture = gameUnit.getModel().modelTexture();
        int textureVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureVboId);
        var textureVertices = BufferUtils.createFloatBuffer(texture.textureVertices());
        glBufferData(GL_ARRAY_BUFFER, textureVertices, GL_STATIC_DRAW);
        var textureAttribute = glGetAttribLocation(getProgramId(), TEXTURE_ATTRIBUTE_NAME);
        glVertexAttribPointer(textureAttribute, 2, GL_FLOAT, false, 0, 0);
        // Unbind textures VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        MemoryUtil.memFree(textureVertices);

        // Unbind the VAO
        glBindVertexArray(0);
        return new DrawableModel(gameUnit, vaoId, vertices);
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
        glUniformMatrix4fv(
                getUniformIdBy(name),
                false,
                matrix4f.toFloatBuffer()
        );
    }

    private int getUniformIdBy(String uniformName) {
        return uniformCache.computeIfAbsent(uniformName, name -> {
            var uniformId = glGetUniformLocation(programId, uniformName);
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
        glUseProgram(programId);
    }

    private void disable() {
        glUseProgram(0);
    }
}
