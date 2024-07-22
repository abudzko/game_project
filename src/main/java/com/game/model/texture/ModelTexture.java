package com.game.model.texture;

import java.nio.FloatBuffer;

public interface ModelTexture {
    int textureId();

    FloatBuffer textureVerticesBuffer();
}
