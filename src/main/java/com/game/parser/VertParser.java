package com.game.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public class VertParser {
    public static void main(String[] args) throws IOException {
//        Texture texture = new Texture("E:\\work\\workspace\\game_project\\src\\main\\resources\\texture\\green.png");
//        System.out.println();
    }

    public static float[] readVertices() {
        float[] vertices = new float[0];
        try {
            vertices = readVertices("E:\\work\\workspace\\game_project\\src\\main\\resources\\test\\tor");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vertices;
    }

    public static float[] readVertices(String path) throws IOException {
        File file = new File(path);

        FileReader fileReader = new FileReader(file);
        StringBuilder temp = new StringBuilder();
        FloatBuffer floatBuffer = FloatBuffer.allocate(1024 * 1024);

        int ch;
        while ((ch = fileReader.read()) != -1) {
            if (ch == 32) {
                floatBuffer.put(Float.valueOf(temp.toString()));
                temp.setLength(0);
            } else {
                temp.append((char) ch);
            }
        }
        floatBuffer.put(Float.valueOf(temp.toString()));

        floatBuffer.position(floatBuffer.position()/3);
        float[] vertices = new float[floatBuffer.position()];
        floatBuffer.flip();
        floatBuffer.get(vertices);
        return vertices;
    }
}
