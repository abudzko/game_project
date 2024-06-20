package com.game.dao;

import com.game.model.Model;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ModelDao {

    private static final VerticesDao verticesDao = new VerticesDao();
    private static final TextureDao textureDao= new TextureDao();


    private static final Map<Integer, ModelWrapper> MODELS = new HashMap<Integer, ModelWrapper>() {
        {
            put(0, new ModelWrapper(
                    0,
                    new Vector3f(0f, 0f, 0f),
                    new Vector3f(0f, 0f, 0f),
                    16
            ));

            put(1, new ModelWrapper(
                    1,
                    new Vector3f(0f, 0f, 0f),
                    new Vector3f(0f, 0f, 0f),
                    0.05f
            ));
        }
    };

    public Model getModel(int id) {
        ModelWrapper modelWrapper = MODELS.get(id);
        Model model = new Model(
                id,
                modelWrapper.position,
                modelWrapper.rotation,
                modelWrapper.scale
        );
        return model;

    }

    static class ModelWrapper {
        final int id;
        final Vector3f position;
        final Vector3f rotation;
        final float scale;

        ModelWrapper(int id, Vector3f position, Vector3f rotation, float scale) {
            this.id = id;
            this.position = position;
            this.rotation = rotation;
            this.scale = scale;
        }
    }
}
