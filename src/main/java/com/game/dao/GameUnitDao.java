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
    private static final long MAIN_UNIT_ID = 0;
    private final List<GameUnit> UNITS = createUnits();
    private final GameUnit MAIN_UNIT = createMainUnit();

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
        var unit3 = createCudeGameUnit(new Vector3f(0f, 0f, 0f));
        gameUnits.add(unit1);
        gameUnits.add(unit2);
        gameUnits.add(unit3);
        return gameUnits;
    }

    public static GameUnit createCudeGameUnit(Vector3f position) {
        return new GameUnit(
                RANDOM.nextLong(),
                position,
                new Vector3f(0f, 0f, 0f),
                0.1f,
                new CubeManualModel()
        );
    }

    private static GameUnit createMainUnit() {
        return new GameUnit(
                MAIN_UNIT_ID,
                new Vector3f(0f, 0.5f, 0f),
                new Vector3f(0f, 0f, 0f),
                0.1f,
                new ObjModel("src/main/resources/obj/monkey.obj")
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
                new Vector3f(0f, 0.5f, 0f),
                new Vector3f(0f, 0f, 0f),
                0.1f,
                new ObjModel("src/main/resources/obj/tor1.obj")
        );
    }
}
