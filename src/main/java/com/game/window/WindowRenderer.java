package com.game.window;

import com.game.dao.TextureDao;
import com.game.dao.VerticesDao;
import com.game.model.Camera;
import com.game.model.Model;
import com.game.model.OnCameraChangedListener;
import com.game.shader.Program;
import com.game.shader.Shader;
import com.game.shader.Texture;
import com.game.utils.math.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public class WindowRenderer implements OnCameraChangedListener {

    private final VerticesDao verticesDao = new VerticesDao();
    private final TextureDao textureDao = new TextureDao();

    private int attributeLocation;
    private int attributeTexture;

    private Program program;

    private static final int POINT_PER_VERTEX_3D = 3;

    /**
     * Angle: camera < visible scope
     */
    private static final float FOV = (float) Math.toRadians(60f);

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 10.f;

    private float aspectRatio;
    private Matrix4f projectionMatrix;

    private Window window;
    private float AR;

    public WindowRenderer(Window window) {
        this.window = window;
        this.program = createProgram();
        this.aspectRatio = (float) window.getWidth() / (float) window.getHeight();
        this.AR = aspectRatio;

        setMatrices();


    }

    private void setMatrices() {
        program.enable();

        projectionMatrix = new Matrix4f();
        projectionMatrix.perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
        program.setUniformMatrix4f("projectionMatrix", projectionMatrix);

        attributeLocation = GL20.glGetAttribLocation(program.getProgramId(), "attributeLocation");
        attributeTexture = GL20.glGetAttribLocation(program.getProgramId(), "attributeTexture");

        program.disable();
    }

    private Program createProgram() {
        Program program = new Program(
                Shader.createVertexShader("src/main/resources/shaders/v.vert"),
                Shader.createFragmentShader("src/main/resources/shaders/f.frag")
        );
        return program;
    }

    public void render(Iterable<DrawableModel> models) {
        GLFW.glfwSwapBuffers(window.getWindowId());
//        GLFW.glfwSwapBuffers(0);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        program.enable();

        for (DrawableModel drawableModel : models) {
            program.setUniformMatrix4f("worldMatrix", drawableModel.getWorldMatrix());
            // Bind to the VAO
            GL30.glBindVertexArray(drawableModel.getVaoId());
            GL20.glEnableVertexAttribArray(attributeLocation);
            GL20.glEnableVertexAttribArray(attributeTexture);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, drawableModel.getTextureId());
            // Draw the vertices
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, drawableModel.getVertices().limit() / POINT_PER_VERTEX_3D);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            // Restore state
            GL20.glDisableVertexAttribArray(attributeLocation);
            GL20.glDisableVertexAttribArray(attributeTexture);
            GL30.glBindVertexArray(0);
        }


//TODO refactor
        if (AR != aspectRatio) {
            aspectRatio = AR;
            projectionMatrix = new Matrix4f();
            projectionMatrix.perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
            program.setUniformMatrix4f("projectionMatrix", projectionMatrix);
        }

        program.disable();
    }

    public DrawableModel createDrawableModel(Model model) {
        int id = model.getId();
        FloatBuffer vertices = verticesDao.getVertices(id);
        Texture texture = textureDao.getTexture(id);

        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(attributeLocation, POINT_PER_VERTEX_3D, GL11.GL_FLOAT, false, 0, 0);

        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // textures
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texture.getTextureVertices(), GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(attributeTexture, 2, GL11.GL_FLOAT, false, 0, 0);
        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);


        // Unbind the VAO
        GL30.glBindVertexArray(0);
        DrawableModel drawableModel = new DrawableModel(model, vaoId, vertices, texture);
        return drawableModel;
    }

    @Override
    public void onCameraChanged(Camera camera) {
        program.enable();
        program.setUniformMatrix4f("viewMatrix", camera.getLookAtMatrix());
        program.disable();
    }

    public Window getWindow() {
        return window;
    }

    public void setAR(float AR) {
        this.AR = AR;
    }
}
