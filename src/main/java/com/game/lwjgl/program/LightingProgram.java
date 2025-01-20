package com.game.lwjgl.program;

import com.game.lwjgl.program.shader.Shader;
import com.game.model.DrawableModel;
import com.game.model.GameUnit;
import com.game.utils.BufferUtils;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL30.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL30.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL30.GL_FLOAT;
import static org.lwjgl.opengl.GL30.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL30.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_TRIANGLES;
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

public class LightingProgram {
    protected static final int POINT_PER_VERTEX_3D = 3;
    protected static final String PROJECTION_MATRIX_NAME = "projectionMatrix";
    protected static final String CAMERA_VIEW_MATRIX_NAME = "cameraViewMatrix";
    protected static final String WORLD_MATRIX_NAME = "worldMatrix";
    protected static final String POSITION_ATTRIBUTE_NAME = "positionAttribute";
    protected static final String NORMAL_ATTRIBUTE_NAME = "normalAttribute";
    protected static final String TEXTURE_ATTRIBUTE_NAME = "textureAttribute";
    protected static final String CAMERA_POSITION_NAME = "cameraPosition";
    protected static final String SHADER_PATH = "src/main/resources/shaders/light/";

    protected static final String LIGHT_COLOR_NAME = "lightColor";
    protected static final String LIGHT_POSITION_NAME = "lightPosition";
    protected static final String IS_LIGHT_NAME = "isLight";
    private final Shader vertexShader;
    private final Shader fragmentShader;
    private final ConcurrentHashMap<String, Integer> uniformCache = new ConcurrentHashMap<>();
    private int programId;
    private Integer positionAttributeId;
    private Integer textureAttributeId;
    private Integer normalAttributeId;

    public LightingProgram() {
        this.vertexShader = new Shader(SHADER_PATH + "vl.vert", GL_VERTEX_SHADER);
        this.fragmentShader = new Shader(SHADER_PATH + "fl.frag", GL_FRAGMENT_SHADER);
        linkProgram();
    }

    private void linkProgram() {
        programId = glCreateProgram();

        glAttachShader(programId, vertexShader.getId());
        glAttachShader(programId, fragmentShader.getId());

        glLinkProgram(programId);
        glValidateProgram(programId);

        releaseResources();
    }

    public void render(RenderObjects renderObjects) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);

        enable();

        DrawableModel lightModel = null;

        for (var drawableModel : renderObjects.getModels()) {
            setUniformMatrix4f(WORLD_MATRIX_NAME, drawableModel.getWorldMatrix());
            if (drawableModel.isLight()) {
                lightModel = drawableModel;
                setUniformInt(IS_LIGHT_NAME, 1);
            } else {
                setUniformInt(IS_LIGHT_NAME, 0);
            }
            // Bind to the VAO
            glBindVertexArray(drawableModel.getVaoId());

            // Enable Texture
            glEnableVertexAttribArray(getTextureAttribute());
            glBindTexture(GL_TEXTURE_2D, drawableModel.getTextureId());

            glEnableVertexAttribArray(getNormalAttribute());

            // Draw the vertices
            glEnableVertexAttribArray(getPositionAttribute());
            int verticesCount = drawableModel.getVertices().limit() / POINT_PER_VERTEX_3D;
            // TODO use indexes
            glDrawArrays(GL_TRIANGLES, 0, verticesCount);

            // Clean resources
            glBindTexture(GL_TEXTURE_2D, 0);
            glDisableVertexAttribArray(getNormalAttribute());
            glDisableVertexAttribArray(getTextureAttribute());
            glDisableVertexAttribArray(getPositionAttribute());
            glBindVertexArray(0);
        }

        if (renderObjects.getDeletedModels() != null) {
            for (var deletedModel : renderObjects.getDeletedModels()) {
                //TODO delete vbo or we can reuse them for the similar units?
                //TODO delete texture id or we can reuse them for the similar units?
                GL30.glDeleteVertexArrays(deletedModel.getVaoId());
            }
        }
        if (renderObjects.getCameraViewMatrix() != null) {
            setUniformMatrix4f(CAMERA_VIEW_MATRIX_NAME, renderObjects.getCameraViewMatrix());
        }
        if (renderObjects.getProjectionMatrix() != null) {
            setUniformMatrix4f(PROJECTION_MATRIX_NAME, renderObjects.getProjectionMatrix());
        }

        // TODO single light is supported
        if (lightModel != null) {
            var light = lightModel.getLight();
            var lightPosition = light.getLightPosition();
            setUniformVec3(LIGHT_POSITION_NAME, new float[]{lightPosition.x, lightPosition.y, lightPosition.z});
            var lightColor = light.getLightColor();
            setUniformVec3(LIGHT_COLOR_NAME, new float[]{lightColor.x, lightColor.y, lightColor.z});
        }

        if (renderObjects.getCameraPosition() != null) {
            setUniformVec3(CAMERA_POSITION_NAME, renderObjects.getCameraPosition());
        }

        disable();
    }

    public DrawableModel createDrawableModel(GameUnit gameUnit) {
        // Load in GPU Memory our model
        // Create VAO per model
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Vertices
        var vertices = gameUnit.getModel().triangleVertices();
        int verticesVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, verticesVboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(getPositionAttribute(), POINT_PER_VERTEX_3D, GL_FLOAT, false, 0, 0);

        // Unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        BufferUtils.memFree(vertices);

        // Textures
        int textureVboId = glGenBuffers();
        var textureVertices = BufferUtils.createFloatBuffer4f(gameUnit.getModel().modelTexture().textureVertices());
        glBindBuffer(GL_ARRAY_BUFFER, textureVboId);
        glBufferData(GL_ARRAY_BUFFER, textureVertices, GL_STATIC_DRAW);
        glVertexAttribPointer(getTextureAttribute(), 2, GL_FLOAT, false, 0, 0);
        // Unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        BufferUtils.memFree(textureVertices);

        // Vertex normals
        var vertexNormals = gameUnit.getModel().triangleVertexNormals();
        if (vertexNormals != null) {
            int normalsVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, normalsVboId);
            glBufferData(GL_ARRAY_BUFFER, vertexNormals, GL_STATIC_DRAW);
            glVertexAttribPointer(getNormalAttribute(), POINT_PER_VERTEX_3D, GL_FLOAT, false, 0, 0);
            // Unbind the VBO
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            BufferUtils.memFree(vertexNormals);
        }

        // Unbind the VAO
        glBindVertexArray(0);
        return new DrawableModel(gameUnit, vaoId, vertices);
    }

    private int getPositionAttribute() {
        if (positionAttributeId == null) {
            positionAttributeId = glGetAttribLocation(getProgramId(), POSITION_ATTRIBUTE_NAME);
        }
        return positionAttributeId;
    }

    private int getTextureAttribute() {
        if (textureAttributeId == null) {
            textureAttributeId = glGetAttribLocation(getProgramId(), TEXTURE_ATTRIBUTE_NAME);
        }
        return textureAttributeId;
    }

    private int getNormalAttribute() {
        if (normalAttributeId == null) {
            normalAttributeId = glGetAttribLocation(getProgramId(), NORMAL_ATTRIBUTE_NAME);
        }
        return normalAttributeId;
    }

    private void releaseResources() {
        vertexShader.deleteShader();
        fragmentShader.deleteShader();
    }

    private void setUniformVec3(String name, float[] vector3f) {
        GL30.glUniform3f(
                getUniformIdBy(name),
                vector3f[0],
                vector3f[1],
                vector3f[2]
        );
    }

    private void setUniformInt(String name, int value) {
        GL30.glUniform1i(
                getUniformIdBy(name),
                value
        );
    }

    private void setUniformMatrix4f(String name, Matrix4f matrix4f) {
        var floatBuffer = BufferUtils.toFloatBuffer(matrix4f);
        glUniformMatrix4fv(
                getUniformIdBy(name),
                false,
                floatBuffer
        );
        BufferUtils.memFree(floatBuffer);
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
        glUseProgram(getProgramId());
    }

    private void disable() {
        glUseProgram(0);
    }
}
