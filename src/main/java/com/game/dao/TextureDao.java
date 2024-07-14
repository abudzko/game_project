package com.game.dao;

import com.game.shader.Texture;
import com.game.utils.BufferUtils;
import com.game.utils.log.LogUtil;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextureDao {
    private static final float[] TEXTURE_VERTICES0 = {
            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1
    };

    private static final float[] TEXTURE_VERTICES1 = {
            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1,

            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1,

            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1,

            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1,

            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1,

            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1,
    };

    private static final float[] TEXTURE_VERTICES2 = {
            0, 0,
            0, 1,
            1, 1,

            0, 0,
            1, 0,
            1, 1
    };

    private static final Map<Integer, TextureWrapper> TEXTURES = new HashMap<Integer, TextureWrapper>() {
        {
            put(0, new TextureWrapper(
                    0,
                    "/texture/green.png",
                    TEXTURE_VERTICES0
            ));

            put(1, new TextureWrapper(
                    1,
                    "/texture/cat512.png",
                    TEXTURE_VERTICES1
            ));

            put(2, new TextureWrapper(
                    2,
                    "/texture/sky.png",
                    TEXTURE_VERTICES2
            ));
        }
    };

    public Texture getTexture(int id) {
        try {
            TextureWrapper textureWrapper = TEXTURES.get(id);
            Texture texture = new Texture(textureWrapper.path);
            FloatBuffer textureVertices = BufferUtils.createFloatBuffer(textureWrapper.textureVertices);
            texture.setTextureVertices(textureVertices);
            return texture;

        } catch (IOException e) {
            LogUtil.logError(String.format("Texture id %s %s", id, e.getMessage()));
        }
        return null;
    }


    private static class TextureWrapper {
        final int id;
        final String path;
        final float[] textureVertices;

        private TextureWrapper(int id, String path, float[] textureVertices) {
            this.id = id;
            this.path = path;
            this.textureVertices = textureVertices;
        }
    }
}
