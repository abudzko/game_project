package com.game.shader;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;

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
            var id = GL11.glGenTextures();

            // Bind the texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

            // Tell opengl how to unpack bytes
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            // Set the texture parameters, can be GL_LINEAR or GL_NEAREST
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

            // Upload texture
            GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL11.GL_RGBA,
                    decoder.getWidth(),
                    decoder.getHeight(),
                    0, GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE,
                    buffer
            );

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
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
