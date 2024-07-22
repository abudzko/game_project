package com.game.model.manual;

import com.game.model.Model;
import com.game.model.texture.ModelTexture;
import com.game.model.texture.SquareTexture;
import com.game.utils.BufferUtils;

import java.nio.FloatBuffer;

public class SkyManualModel implements Model {

    private static final float[] SKY_VERTICES = {
            -1f, 0f, -1f,
            -1f, 0f, 1f,
            1f, 0f, 1f,
            -1f, 0f, -1f,
            1f, 0f, -1f,
            1f, 0f, 1f
    };

    private ModelTexture modelTexture;

    public SkyManualModel() {

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
        return BufferUtils.createFloatBuffer(SKY_VERTICES);
    }

    @Override
    public ModelTexture modelTexture() {
        if (modelTexture == null) {
            modelTexture = new SquareTexture("/texture/sky.png");
        }
        return modelTexture;
    }
}
