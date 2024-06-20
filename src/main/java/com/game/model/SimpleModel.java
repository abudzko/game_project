package com.game.model;

import com.game.shader.Texture;
import org.joml.Vector3f;

import java.io.IOException;

public class SimpleModel extends Model {
    private static final Vector3f POSITION = new Vector3f(0f, 0f, 0f);
    private static final Vector3f ROTATION = new Vector3f(0f, 0f, 0f);
    private static final int SCALE = 3;
    private static final float[] EMPTY = {};
    private static final float z = 0f;
    private static final float y = 0f;
    private static final float[] VERTICES = {
            -1f, y, -1f,
            -1f, y, 1f,
            1f, y, 1f,
            -1f, y, -1f,
            1f, y, -1f,
            1f, y, 1f
    };

    private static final float[] TEXTURE_COORDS = {
            0, 0,
            0, 1,
            1, 1,
//            1, 0,

            0, 0,
            1, 0,
            1, 1,
//            0, 1
    };

    private static Texture texture;
    private static Texture texture4;

    {
        try {
            texture = new Texture("/texture/green.png");
            texture4 = new Texture("/texture/red.png");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public SimpleModel() {
        super(0, POSITION, ROTATION, SCALE);
    }
}
