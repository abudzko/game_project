package com.game.dao;

import com.game.model.GameUnit;
import com.game.model.ObjModel;
import com.game.model.manual.CubeManualModel;
import com.game.model.manual.GroundManualModel;
import com.game.model.manual.SkyManualModel;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUnitDao {
    protected static final Random RANDOM = new Random();
    private static final List<GameUnit> UNITS = createUnits();
    private static final long MAIN_UNIT_ID = 0;
    private static final GameUnit MAIN_UNIT = createMainUnit();

    private static List<GameUnit> createUnits() {
        var gameUnits = new ArrayList<GameUnit>();
        var unit1 = new GameUnit(
                1,
                new Vector3f(0f, 0f, 0f),
                new Vector3f(0f, 0f, 0f),
                10f,
                new GroundManualModel()
        );
        var unit2 = new GameUnit(
                2,
                new Vector3f(0f, 0f, -10f),
                new Vector3f(90f, 0f, 0f),
                10f,
                new SkyManualModel()
        );
        var unit3 = new GameUnit(
                3,
                new Vector3f(-.5f, .5f, 0f),
                new Vector3f(0f, 0f, 0f),
                0.05f,
                new ObjModel("src/main/resources/obj/tor.obj")
        );
        gameUnits.add(unit1);
        gameUnits.add(unit2);
        gameUnits.add(unit3);
        return gameUnits;
    }

    private static GameUnit createMainUnit() {
        return new GameUnit(
                MAIN_UNIT_ID,
                new Vector3f(0f, 0f, 0f),
                new Vector3f(0f, 0f, 0f),
                0.05f,
                new CubeManualModel()
        );
    }

    public List<GameUnit> getUnits() {
        return UNITS;
    }

    public GameUnit getMainUnit() {
        return MAIN_UNIT;
    }

    public GameUnit createGameUnit() {
        long id = RANDOM.nextLong();
        return new GameUnit(
                id,
                new Vector3f(0f, 0f, 0f),
                new Vector3f(0f, 0f, 0f),
                0.05f,
                new CubeManualModel()
        );
    }
}
