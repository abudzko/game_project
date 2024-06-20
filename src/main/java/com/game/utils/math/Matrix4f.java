package com.game.utils.math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Matrix4f extends org.joml.Matrix4f {
    private final FloatBuffer floatBuffer;

    public Matrix4f() {
        super();
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 4 * Float.BYTES);
        buffer.order(ByteOrder.nativeOrder());
        floatBuffer = buffer.asFloatBuffer();
    }

    public FloatBuffer toFloatBuffer() {
        floatBuffer.clear();

        floatBuffer.put(m00());
        floatBuffer.put(m01());
        floatBuffer.put(m02());
        floatBuffer.put(m03());
        floatBuffer.put(m10());
        floatBuffer.put(m11());
        floatBuffer.put(m12());
        floatBuffer.put(m13());
        floatBuffer.put(m20());
        floatBuffer.put(m21());
        floatBuffer.put(m22());
        floatBuffer.put(m23());
        floatBuffer.put(m30());
        floatBuffer.put(m31());
        floatBuffer.put(m32());
        floatBuffer.put(m33());

        floatBuffer.flip();
        return floatBuffer;
    }
}
