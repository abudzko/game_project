package com.game.shader;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PngTexture {

    private final String imagePath;
    private Integer textureId;

    public PngTexture(String imagePath) {
        this.imagePath = imagePath;
        loadTexture();
    }

    private void loadTexture() {
        try {
            // Load png file
            var decoder = new PNGDecoder(getClass().getResourceAsStream(imagePath));
            // Create a byte buffer big enough to store RGBA values
            var buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

            // Decode
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);

            // Flip the buffer(prepare for reading)
            buffer.flip();

            // Create a texture
            var id = GL30.glGenTextures();

            // Bind the texture
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, id);

            // Tell opengl how to unpack bytes
            GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);

            // Set the texture parameters, can be GL_LINEAR or GL_NEAREST
            GL30.glTexParameterf(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
            GL30.glTexParameterf(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);

            // Upload texture
            GL30.glTexImage2D(
                    GL30.GL_TEXTURE_2D,
                    0,
                    GL30.GL_RGBA,
                    decoder.getWidth(),
                    decoder.getHeight(),
                    0, GL30.GL_RGBA,
                    GL30.GL_UNSIGNED_BYTE,
                    buffer
            );

            GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
            textureId = id;
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Failed to load image %s", imagePath));
        }
    }

    public int getTextureId() {
        if (textureId == null) {
            throw new IllegalStateException(String.format("Texture [%s] was not loaded", imagePath));
        }
        return textureId;
    }
}
