package com.game.model;


import com.game.model.texture.ModelTexture;
import com.game.model.texture.ObjTexture;
import com.game.utils.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents Blender .obj file
 * Model should have Triangulate (Beauty-Beauty) modifier
 * When model is exported check-boxes Apply-Modifier and Triangulate-Mesh should be checked
 */
public class ObjModel implements Model {
    protected static final String LF = "\n";
    protected static final String WHITE_SPACE = " ";
    protected static final String SLASH = "/";
    private final String objPath;
    private float[] vertices;
    private int[] indexes;
    private float[] triangleVertices;
    private float[] triangleTextureVertices;

    private ModelTexture modelTexture;

    public ObjModel(String objPath) {
        this.objPath = objPath;
    }

    public static void main(String[] args) {
        var obj = new ObjModel("src/main/resources/obj/t.obj");
        obj.parseObj();
        System.out.println();
    }

    private static String resource(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Failed to load resource %s", path), e);
        }
    }

    private void parseObj() {
        var objStr = resource(objPath);
        var verticesMap = new HashMap<Integer, Vertex>();
        var textureVerticesMap = new HashMap<Integer, TextureVertex>();
        int verticesIndex = 1;
        var triangleList = new ArrayList<Triangle>();
        var textureTriangleList = new ArrayList<TextureTriangle>();
        int textureVerticesIndex = 1;
        var lines = objStr.split(LF);
        for (var line : lines) {
            var elements = line.split(WHITE_SPACE);
            var elementIndex = 0;
            var type = elements[elementIndex++];
            switch (type) {
                // Vertices
                case "v":
                    var vertex = new Vertex();
                    vertex.x = elements[elementIndex++];
                    vertex.y = elements[elementIndex++];
                    vertex.z = elements[elementIndex];
                    verticesMap.put(verticesIndex++, vertex);
                    break;
                // Texture vertices
                case "vt":
                    var textureVertex = new TextureVertex();
                    textureVertex.x = elements[elementIndex++];
                    textureVertex.y = elements[elementIndex];
                    textureVerticesMap.put(textureVerticesIndex++, textureVertex);
                    break;
                // Faces
                case "f":
                    var triangle = new Triangle();
                    var element1Indexes = extractIndexes(elements[elementIndex++]);
                    var element2Indexes = extractIndexes(elements[elementIndex++]);
                    var element3Indexes = extractIndexes(elements[elementIndex]);

                    triangle.v1 = verticesMap.get(element1Indexes.get(0));
                    triangle.v2 = verticesMap.get(element2Indexes.get(0));
                    triangle.v3 = verticesMap.get(element3Indexes.get(0));
                    triangleList.add(triangle);

                    var textureTriangle = new TextureTriangle();
                    textureTriangle.v1 = textureVerticesMap.get(element1Indexes.get(1));
                    textureTriangle.v2 = textureVerticesMap.get(element2Indexes.get(1));
                    textureTriangle.v3 = textureVerticesMap.get(element3Indexes.get(1));
                    textureTriangleList.add(textureTriangle);
                    break;
                default:
                    break;
            }
        }
        triangleVertices = new float[triangleList.size() * 9];
        int i = 0;
        for (var triangle : triangleList) {
            addTriangle(triangle, i);
            i += 9;
        }

        triangleTextureVertices = new float[textureTriangleList.size() * 9];
        i = 0;
        for (var textureTriangle : textureTriangleList) {
            addTextureTriangle(textureTriangle, i);
            i += 9;
        }
    }


    private List<Integer> extractIndexes(String element) {
        return Arrays.stream(element.split(SLASH)).map(Integer::valueOf).collect(Collectors.toList());
    }

    private void addTriangle(Triangle triangle, int index) {
        addVertex(triangle.v1, index);
        addVertex(triangle.v2, index + 3);
        addVertex(triangle.v3, index + 6);
    }

    private void addVertex(Vertex v, int index) {
        triangleVertices[index++] = Float.parseFloat(v.x);
        triangleVertices[index++] = Float.parseFloat(v.y);
        triangleVertices[index] = Float.parseFloat(v.z);
    }

    private void addTextureTriangle(TextureTriangle textureTriangle, int index) {
        addTextureTriangle(textureTriangle.v1, index);
        addTextureTriangle(textureTriangle.v2, index + 3);
        addTextureTriangle(textureTriangle.v3, index + 6);
    }

    private void addTextureTriangle(TextureVertex textureVertex, int index) {
        triangleTextureVertices[index++] = Float.parseFloat(textureVertex.x);
        triangleTextureVertices[index] = Float.parseFloat(textureVertex.y);
    }

    @Override
    public float[] vertices() {
        if (vertices == null) {
            parseObj();
        }
        return vertices;
    }

    @Override
    public int[] indexes() {
        if (indexes == null) {
            parseObj();
        }
        return indexes;
    }

    @Override
    public FloatBuffer triangleVertices() {
        if (triangleVertices == null) {
            parseObj();
        }
        return BufferUtils.createFloatBuffer4f(triangleVertices);
    }

    @Override
    public ModelTexture modelTexture() {
        if (modelTexture == null) {
            parseObj();
            modelTexture = new ObjTexture("/texture/cat512.png", triangleTextureVertices);
        }
        return modelTexture;
    }

    private static class Vertex {
        private String x;
        private String y;
        private String z;
    }

    private static class Triangle {
        private Vertex v1;
        private Vertex v2;
        private Vertex v3;
    }

    private static class TextureVertex {
        private String x;
        private String y;
    }

    private static class TextureTriangle {
        private TextureVertex v1;
        private TextureVertex v2;
        private TextureVertex v3;
    }
}
