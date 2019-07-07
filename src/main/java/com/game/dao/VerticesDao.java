package com.game.dao;

import com.game.utils.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class VerticesDao {

    private static final float y = 0f;
    private static final float[] VERTICES0 = {
            -1f, y, -1f,
            -1f, y, 1f,
            1f, y, 1f,
            -1f, y, -1f,
            1f, y, -1f,
            1f, y, 1f
    };

    private static final float[] VERTICES1 = {
            //front
            -0.5f, y, 0f,
            0.5f, y, 0f,
            0.5f, 0.8f, 0f,
            0.5f, 0.8f, 0f,
            -0.5f, 0.8f, 0f,
            -0.5f, y, 0f,

            //left
            -0.5f, y, 0f,
            -0.5f, y, -1f,
            -0.5f, 0.8f, 0f,
            -0.5f, 0.8f, 0f,
            -0.5f, 0.8f, -1f,
            -0.5f, y, -1f,

            //right
            0.5f, 0.8f, 0f,
            0.5f, y, 0f,
            0.5f, y, -1f,
            0.5f, 0.8f, 0f,
            0.5f, 0.8f, -1f,
            0.5f, y, -1f,

            //back
            -0.5f, y, -1f,
            0.5f, y, -1f,
            0.5f, 0.8f, -1f,
            0.5f, 0.8f, -1f,
            -0.5f, 0.8f, -1f,
            -0.5f, y, -1f,

            //top
            0.5f, 0.8f, 0f,
            0.5f, 0.8f, -1f,
            -0.5f, 0.8f, -1f,
            0.5f, 0.8f, 0f,
            -0.5f, 0.8f, -1f,
            -0.5f, 0.8f, 0f,

            //bottom
            0.5f, y, 0f,
            0.5f, y, -1f,
            -0.5f, y, -1f,
            -0.5f, y, -1f,
            -0.5f, y, 0f,
            0.5f, y, 0f
    };

    private static final List<float[]> VERTICES = new ArrayList<float[]>() {
        {
            add(VERTICES0);
            add(VERTICES1);
        }
    };

    public FloatBuffer getVertices(int id) {
        return BufferUtils.createFloatBuffer(VERTICES.get(id));
    }
}
