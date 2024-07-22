package com.game.dao;

import com.game.model.GameUnit;
import com.game.model.ObjModel;
import com.game.model.manual.CubeManualModel;
import com.game.model.manual.GroundManualModel;
import com.game.model.manual.SkyManualModel;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ModelDao {
    private static final Map<Long, GameUnit> MODELS = createModels();

    private static Map<Long, GameUnit> createModels() {
        var models = new HashMap<Long, GameUnit>();
        var m0 = new GameUnit(
                0,
                new Vector3f(0f, 0f, 0f),
                new Vector3f(0f, 0f, 0f),
                10f,
                new GroundManualModel()
        );

        var m1 = new GameUnit(
                1,
                new Vector3f(0f, 0f, 0f),
                new Vector3f(0f, 0f, 0f),
                0.05f,
                new CubeManualModel()
        );
        var m2 = new GameUnit(
                2,
                new Vector3f(0f, 0f, -10f),
                new Vector3f(90f, 0f, 0f),
                10f,
                new SkyManualModel()
        );
        var m3 = new GameUnit(
                3,
                new Vector3f(-.5f, .5f, 0f),
                new Vector3f(0f, 0f, 0f),
                0.05f,
                new ObjModel("src/main/resources/obj/tor.obj")
        );
        models.put(m0.getId(), m0);
        models.put(m1.getId(), m1);
        models.put(m2.getId(), m2);
        models.put(m3.getId(), m3);
        return models;
    }

    public GameUnit findGameUnit(long id) {
        return MODELS.get(id);
    }
}
