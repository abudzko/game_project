package com.game.utils;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {

    public static ByteBuffer createByteBuffer(byte[] array) {
        return MemoryUtil.memAlloc(array.length).put(array).flip();
    }

    public static FloatBuffer createFloatBuffer(float[] array) {
        return MemoryUtil.memAllocFloat(array.length).put(array).flip();
    }

    public static IntBuffer createIntBuffer(int[] array) {
        return MemoryUtil.memAllocInt(array.length).put(array).flip();
    }
}
