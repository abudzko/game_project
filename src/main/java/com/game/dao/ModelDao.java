package com.game.dao;

import com.game.model.Model;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ModelDao {

    private static final VerticesDao verticesDao = new VerticesDao();
    private static final TextureDao textureDao = new TextureDao();

    private static final Map<Integer, Model> MODELS = createModels();

    public Model getModel(int id) {
        return MODELS.get(id);
    }

    private static Map<Integer, Model> createModels() {
        var models = new HashMap<Integer, Model>();
        var m0 = new Model(
                0,
                new Vector3f(0f, 0f, 0f),
                new Vector3f(0f, 0f, 0f),
                1f
        );

        var m1 = new Model(
                1,
                new Vector3f(0f, 0f, 0f),
                new Vector3f(0f, 0f, 0f),
                0.05f
        );
        var m2 = new Model(
                2,
                new Vector3f(0f, 0f, -1f),
                new Vector3f(90f, 0f, 0f),
                1f
        );
        models.put(m0.getId(), m0);
        models.put(m1.getId(), m1);
        models.put(m2.getId(), m2);
        return models;
    }
}
