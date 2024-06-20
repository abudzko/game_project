package com.game.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

public class VertParser {
    public static void main(String[] args) throws IOException {
//        Texture texture = new Texture("E:\\work\\workspace\\game_project\\src\\main\\resources\\texture\\green.png");
//        System.out.println();
    }

    public static float[] readVertices() {
        float[] vertices = new float[0];
        try {
            vertices = readVertices("/test/tor");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vertices;
    }

    public static float[] readVertices(String path) throws IOException {
        File file = new File(path);
        InputStream inputStream = VertParser.class.getClassLoader().getResourceAsStream(path);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        StringBuilder temp = new StringBuilder();
        FloatBuffer floatBuffer = FloatBuffer.allocate(1024 * 1024);

        int ch;
        while ((ch = bufferedInputStream.read()) != -1) {
            if (ch == 32) {
                floatBuffer.put(Float.valueOf(temp.toString()));
                temp.setLength(0);
            } else {
                temp.append((char) ch);
            }
        }
        floatBuffer.put(Float.valueOf(temp.toString()));

        floatBuffer.position(floatBuffer.position() / 3);
        float[] vertices = new float[floatBuffer.position()];
        floatBuffer.flip();
        floatBuffer.get(vertices);
        return vertices;
    }
}
