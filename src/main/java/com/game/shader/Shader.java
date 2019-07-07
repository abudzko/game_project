package com.game.shader;

import com.game.utils.FileUtils;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;


public class Shader {
    private int id;

    public static Shader createVertexShader(String path) {
        return new Shader(path, GL20.GL_VERTEX_SHADER);
    }

    public static Shader createFragmentShader(String path) {
        return new Shader(path, GL20.GL_FRAGMENT_SHADER);
    }

    public Shader(String path, int type) {
        String shaderSource = FileUtils.loadAsString(path);
        id = GL20.glCreateShader(type);
        GL20.glShaderSource(id, shaderSource);
        GL20.glCompileShader(id);
        glCompileShader(id);
        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(String.format("Failed to compile [%s] shader!", type));
            System.err.println(glGetShaderInfoLog(id));
            return;
        }
    }

    public void deleteShader() {
        GL20.glDeleteShader(id);
        id = -1;
    }

    public int getId() {
        return id;
    }
}
