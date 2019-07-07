package com.game.model;

import com.game.parser.VertParser;
import org.joml.Vector3f;

public class TestModel extends Model {
    //    private static final float[] VERTICES = {
//            //front
//            -0.5f, -0.8f, 0f, 1f,
//            0.5f, -0.8f, 0f, 1f,
//            0.5f, 0.8f, 0f, 1f,
//            0.5f, 0.8f, 0f, 1f,
//            -0.5f, 0.8f, 0f, 1f,
//            -0.5f, -0.8f, 0f, 1f,
//
//            //left
//            -0.5f, -0.8f, 0f, 1f,
//            -0.5f, -0.8f, -1f, 1f,
//            -0.5f, 0.8f, 0f, 1f,
//            -0.5f, 0.8f, 0f, 1f,
//            -0.5f, 0.8f, -1f, 1f,
//            -0.5f, -0.8f, -1f, 1f,
//
//            //right
//            0.5f, 0.8f, 0f, 1f,
//            0.5f, -0.8f, 0f, 1f,
//            0.5f, -0.8f, -1f, 1f,
//            0.5f, 0.8f, 0f, 1f,
//            0.5f, 0.8f, -1f, 1f,
//            0.5f, -0.8f, -1f, 1f,
//
//            //back
//            -0.5f, -0.8f, -1f, 1f,
//            0.5f, -0.8f, -1f, 1f,
//            0.5f, 0.8f, -1f, 1f,
//            0.5f, 0.8f, -1f, 1f,
//            -0.5f, 0.8f, -1f, 1f,
//            -0.5f, -0.8f, -1f, 1f,
//
//            //top
//            0.5f, 0.8f, 0f, 1f,
//            0.5f, 0.8f, -1f, 1f,
//            -0.5f, 0.8f, -1f, 1f,
//            0.5f, 0.8f, 0f, 1f,
//            -0.5f, 0.8f, -1f, 1f,
//            -0.5f, 0.8f, 0f, 1f,
//
//            //bottom
//            0.5f, -0.8f, 0f, 1f,
//            0.5f, -0.8f, -1f, 1f,
//            -0.5f, -0.8f, -1f, 1f,
//            -0.5f, -0.8f, -1f, 1f,
//            -0.5f, -0.8f, 0f, 1f,
//            0.5f, -0.8f, 0f, 1f,
//    };
    private static final float[] VERTICES = VertParser.readVertices();
//    private static final float[] VERTICES = {
//            //front
//            -0.5f, -0.8f, 0f,
//            0.5f, -0.8f, 0f,
//            0.5f, 0.8f, 0f,
//            0.5f, 0.8f, 0f,
//            -0.5f, 0.8f, 0f,
//            -0.5f, -0.8f, 0f,
//
//            //left
//            -0.5f, -0.8f, 0f,
//            -0.5f, -0.8f, -1f,
//            -0.5f, 0.8f, 0f,
//            -0.5f, 0.8f, 0f,
//            -0.5f, 0.8f, -1f,
//            -0.5f, -0.8f, -1f,
//
//            //right
//            0.5f, 0.8f, 0f,
//            0.5f, -0.8f, 0f,
//            0.5f, -0.8f, -1f,
//            0.5f, 0.8f, 0f,
//            0.5f, 0.8f, -1f,
//            0.5f, -0.8f, -1f,
//
//            //back
//            -0.5f, -0.8f, -1f,
//            0.5f, -0.8f, -1f,
//            0.5f, 0.8f, -1f,
//            0.5f, 0.8f, -1f,
//            -0.5f, 0.8f, -1f,
//            -0.5f, -0.8f, -1f,
//
//            //top
//            0.5f, 0.8f, 0f,
//            0.5f, 0.8f, -1f,
//            -0.5f, 0.8f, -1f,
//            0.5f, 0.8f, 0f,
//            -0.5f, 0.8f, -1f,
//            -0.5f, 0.8f, 0f,
//
//            //bottom
//            0.5f, -0.8f, 0f,
//            0.5f, -0.8f, -1f,
//            -0.5f, -0.8f, -1f,
//            -0.5f, -0.8f, -1f,
//            -0.5f, -0.8f, 0f,
//            0.5f, -0.8f, 0f
//    };

    private static final float[] COLORS = {
            1, 0, 0f, 1f,  //red
            1, 0, 0f, 1f,  //red
            1, 0, 0f, 1f,  //red
            1, 0, 0f, 1f,  //red
            1, 0, 0f, 1f,  //red
            1, 0, 0f, 1f,  //red

            0f, 0f, 1f, 1f,  //blue
            0f, 0f, 1f, 1f,  //blue
            0f, 0f, 1f, 1f,  //blue
            0f, 0f, 1f, 1f,  //blue
            0f, 0f, 1f, 1f,  //blue
            0f, 0f, 1f, 1f,  //blue

            0f, 1f, 0f, 1f, //green
            0f, 1f, 0f, 1f, //green
            0f, 1f, 0f, 1f, //green
            0f, 1f, 0f, 1f, //green
            0f, 1f, 0f, 1f, //green
            0f, 1f, 0f, 1f, //green

            1f, 0f, 1f, 1f, //purple
            1f, 0f, 1f, 1f, //purple
            1f, 0f, 1f, 1f, //purple
            1f, 0f, 1f, 1f, //purple
            1f, 0f, 1f, 1f, //purple
            1f, 0f, 1f, 1f, //purple

            1f, 1f, 0f, 1f, //yellow
            1f, 1f, 0f, 1f, //yellow
            1f, 1f, 0f, 1f, //yellow
            1f, 1f, 0f, 1f, //yellow
            1f, 1f, 0f, 1f, //yellow
            1f, 1f, 0f, 1f, //yellow

            0f, 0f, 0f, 1f, //black
            0f, 0f, 0f, 1f, //black
            0f, 0f, 0f, 1f, //black
            0f, 0f, 0f, 1f, //black
            0f, 0f, 0f, 1f, //black
            0f, 0f, 0f, 1f, //black
    };

    private static final float[] EMPTY = {};
    private static final Vector3f POSITION = new Vector3f(0f, 0f, -1f);
    private static final Vector3f ROTATION = new Vector3f(0f, 0f, 0f);
    //    private static final Vector3f ROTATION = new Vector3f(30f, 45f, 60f);
    private static final int SCALE = 1;

    public TestModel() {
        super(1, POSITION, ROTATION, 1);
    }
}
