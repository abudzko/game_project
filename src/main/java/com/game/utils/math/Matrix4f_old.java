package com.game.utils.math;

import com.game.utils.BufferUtils;

import java.nio.FloatBuffer;

public class Matrix4f_old {
    public static final int SIZE = 4 * 4;
    public float[] elements = new float[SIZE];

    public Matrix4f_old() {

    }

    public static Matrix4f_old identity() {

        Matrix4f_old result = new Matrix4f_old();
        for (int i = 0; i < SIZE; i++) {
            result.elements[i] = 0.0f;
        }
        result.elements[0 + 0 * 4] = 1.0f;
        result.elements[1 + 1 * 4] = 1.0f;
        result.elements[2 + 2 * 4] = 1.0f;
        result.elements[3 + 3 * 4] = 1.0f;


        return result;
    }
    private static final float FOV = 45;
    private static final float NEAR_PLANE = 0.05f;
    private static final float FAR_PLANE = 1000;

    public static Matrix4f_old projectionMatrix() {
        Matrix4f_old result = identity();
        float aspectRatio = (float) 16 / (float) 9 ;

        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;


        result.elements[0] =x_scale;

        result.elements[5] = y_scale;

        result.elements[10] = -((FAR_PLANE + NEAR_PLANE) / frustum_length);;
        result.elements[11] = -1;

        result.elements[14] = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);

        return result;
    }

    public static Matrix4f_old translate(Vector3f_old vector) {
        Matrix4f_old result = identity();
        result.elements[0 + 3 * 4] = vector.x;
        result.elements[1 + 3 * 4] = vector.y;
        result.elements[2 + 3 * 4] = vector.z;
        return result;
    }

    public static Matrix4f_old rotate(float angle) {
        Matrix4f_old result = identity();
        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);

        result.elements[0] = cos;
        result.elements[1] = sin;

        result.elements[4] = -sin;
        result.elements[5] = cos;

        return result;
    }

    public Matrix4f_old multiply(Matrix4f_old matrix) {
        Matrix4f_old result = new Matrix4f_old();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                float sum = 0.0f;
                for (int e = 0; e < 4; e++) {
                    sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
                }
                result.elements[x + y * 4] = sum;
            }
        }
        return result;
    }

    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }

}
