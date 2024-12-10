package com.game.model.texture;

import com.game.lwjgl.texture.PngTexture;

/**
 * Manually created texture
 */
public class SquareTexture implements ModelTexture {
    private static final float[] SQUARE_TEXTURE_VERTICES = {
            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1
    };
    private final PngTexture texture;

    public SquareTexture(String imagePath) {
        this.texture = new PngTexture(imagePath);
    }

    @Override
    public int textureId() {
        return texture.getTextureId();
    }

    @Override
    public float[] textureVertices() {
        return SQUARE_TEXTURE_VERTICES;
    }
}
