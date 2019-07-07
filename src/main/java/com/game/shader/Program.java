package com.game.shader;

import com.game.utils.math.Matrix4f;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;
import java.util.Map;

public class Program {
    private final int programId;

    private final Shader vertexShader;
    private final Shader fragmentShader;

    private Map<String, Integer> uniformLocationCache = new HashMap<>();

    public Program(Shader vertexShader, Shader fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        programId = createProgram();
    }

    public int createProgram() {
        int programId = GL20.glCreateProgram();

        GL20.glAttachShader(programId, vertexShader.getId());
        GL20.glAttachShader(programId, fragmentShader.getId());

        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);

        releaseResource();
        return programId;
    }

    private void releaseResource() {
        vertexShader.deleteShader();
        fragmentShader.deleteShader();
    }

    public int getProgramId() {
        return programId;
    }

    public void enable() {
        GL20.glUseProgram(programId);
    }

    public void disable() {
        GL20.glUseProgram(0);
    }

    public int getUniformLocation(String uniformLocationName) {
        Integer uniformLocation = uniformLocationCache.get(uniformLocationName);
        if (uniformLocation == null) {
            uniformLocation = GL20.glGetUniformLocation(programId, uniformLocationName);
            if (uniformLocation == -1) {
                throw new IllegalArgumentException(String.format(
                        "Could not find uniform location by name:%s"
                        , uniformLocationName
                ));
            }
            uniformLocationCache.put(uniformLocationName, uniformLocation);
        }
        return uniformLocation;
    }

    public void setUniformLocation1i(String name, int value) {
        GL20.glUniform1i(getUniformLocation(name), value);
    }

    public void setUniformLocation1f(String name, float value) {
        GL20.glUniform1f(getUniformLocation(name), value);
    }

    public void setUniformLocation2f(String name, float x, float y) {
        GL20.glUniform2f(getUniformLocation(name), x, y);
    }

    public void setUniformMatrix4f(String name, Matrix4f matrix4f) {
        GL20.glUniformMatrix4fv(
                getUniformLocation(name),
                false,
                matrix4f.toFloatBuffer()
        );
    }
}
