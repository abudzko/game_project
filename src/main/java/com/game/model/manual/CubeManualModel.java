package com.game.model.manual;

import com.game.model.Model;
import com.game.model.texture.CubeTexture;
import com.game.model.texture.ModelTexture;
import com.game.utils.BufferUtils;

import java.nio.FloatBuffer;

public class CubeManualModel implements Model {

    private static final float[] CUBE_VERTICES = {
            //front
            -0.5f, 0f, 0f,
            0.5f, 0f, 0f,
            0.5f, 0.8f, 0f,
            0.5f, 0.8f, 0f,
            -0.5f, 0.8f, 0f,
            -0.5f, 0f, 0f,

            //left
            -0.5f, 0f, 0f,
            -0.5f, 0f, -1f,
            -0.5f, 0.8f, 0f,
            -0.5f, 0.8f, 0f,
            -0.5f, 0.8f, -1f,
            -0.5f, 0f, -1f,

            //right
            0.5f, 0.8f, 0f,
            0.5f, 0f, 0f,
            0.5f, 0f, -1f,
            0.5f, 0.8f, 0f,
            0.5f, 0.8f, -1f,
            0.5f, 0f, -1f,

            //back
            -0.5f, 0f, -1f,
            0.5f, 0f, -1f,
            0.5f, 0.8f, -1f,
            0.5f, 0.8f, -1f,
            -0.5f, 0.8f, -1f,
            -0.5f, 0f, -1f,

            //top
            0.5f, 0.8f, 0f,
            0.5f, 0.8f, -1f,
            -0.5f, 0.8f, -1f,
            0.5f, 0.8f, 0f,
            -0.5f, 0.8f, -1f,
            -0.5f, 0.8f, 0f,

            //bottom
            0.5f, 0f, 0f,
            0.5f, 0f, -1f,
            -0.5f, 0f, -1f,
            -0.5f, 0f, -1f,
            -0.5f, 0f, 0f,
            0.5f, 0f, 0f
    };

    private ModelTexture modelTexture;

    public CubeManualModel() {

    }

    @Override
    public float[] vertices() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int[] indexes() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public FloatBuffer triangleVertices() {
        return BufferUtils.createFloatBuffer(CUBE_VERTICES);
    }

    @Override
    public ModelTexture modelTexture() {
        if (modelTexture == null) {
            modelTexture = new CubeTexture("/texture/cat512.png");
        }
        return modelTexture;
    }
}
