package com.game.model.texture;

import com.game.shader.PngTexture;
import com.game.utils.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Manually created texture
 */
public class SquareTexture implements ModelTexture {
    private static final float[] SQUARE_TEXTURE = {
            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1
    };
    private final PngTexture texture;
    private final FloatBuffer textureVerticesBuffer;

    public SquareTexture(String imagePath) {
        this.texture = new PngTexture(imagePath);
        this.textureVerticesBuffer = BufferUtils.createFloatBuffer(SQUARE_TEXTURE);
    }

    @Override
    public int textureId() {
        return texture.getTextureId();
    }

    @Override
    public FloatBuffer textureVerticesBuffer() {
        return textureVerticesBuffer;
    }
}
