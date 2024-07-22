package com.game.model;


import com.game.model.texture.ModelTexture;
import com.game.model.texture.SquareTexture;
import com.game.utils.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

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

    private ModelTexture modelTexture;

    public ObjModel(String objPath) {
        this.objPath = objPath;
    }

    private static Integer extractVertexIndex(String element) {
        return Integer.valueOf(element.split(SLASH)[0]);
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
        int verticesIndex = 1;
        var triangleList = new ArrayList<Triangle>();
        var lines = objStr.split(LF);
        for (var line : lines) {
            var elements = line.split(WHITE_SPACE);
            var elementIndex = 0;
            var type = elements[elementIndex++];
            switch (type) {
                case "v":
                    var vertex = new Vertex();
                    vertex.x = elements[elementIndex++];
                    vertex.y = elements[elementIndex++];
                    vertex.z = elements[elementIndex];
                    verticesMap.put(verticesIndex++, vertex);
                    break;
                case "f":
                    var triangle = new Triangle();
                    triangle.v1 = verticesMap.get(extractVertexIndex(elements[elementIndex++]));
                    triangle.v2 = verticesMap.get(extractVertexIndex(elements[elementIndex++]));
                    triangle.v3 = verticesMap.get(extractVertexIndex(elements[elementIndex]));
                    triangleList.add(triangle);
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
        return BufferUtils.createFloatBuffer(triangleVertices);
    }

    @Override
    public ModelTexture modelTexture() {
        // FIXME read from obj file
        if (modelTexture == null) {
            modelTexture = new SquareTexture("/texture/red.png");
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
}
