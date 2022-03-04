package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TDGameTest {

    Grid grid;
    TDGame game;
    Tower t;

    @BeforeEach
    void setup() {
        grid = new Grid();
        game = new TDGame(grid, 100);

    }

    @Test
    void testTick() {
        game.placeTower(new GridPosition(0,0));

        for (int i = 0; i <= WaveManager.SECONDS_BETWEEN_WAVES * TDGame.TICKS_PER_SECOND; i++) {
            game.tick();
        }

        assertEquals(game.getWaveManager().getNumEnemiesToSpawn(), WaveManager.BASE_ENEMIES);
    }

    @Test
    void testPlaceTowerOffMap() {
        game.placeTower(new GridPosition(-2, -2));
        assertEquals(game.getTowers().size(), 0);
    }

    @Test
    void testPlaceTowerNonTowerSquare() {
        game.placeTower(new GridPosition(1, 1));
        assertEquals(game.getTowers().size(), 0);
    }

    @Test
    void testPlaceTowerValidLocation() {
        game.placeTower(new GridPosition(1, 0));
        assertEquals(game.getTowers().size(), 1);
    }

    @Test
    void testPlaceTowerInsufficientMoney() {
        game.addMoney(-game.getMoney());
        game.placeTower(new GridPosition(1, 0));
        assertEquals(game.getTowers().size(), 0);
    }

    @Test
    void testPlaceTowerJustEnoughMoney() {
        game.addMoney(-game.getMoney());
        game.addMoney(Tower.COST);
        game.placeTower(new GridPosition(1, 0));
        assertEquals(game.getTowers().size(), 1);
    }

    @Test
    void testPlaceTowerMoreThanEnoughMoney() {
        game.addMoney(Tower.COST + 1);
        game.placeTower(new GridPosition(1, 0));
        assertEquals(game.getTowers().size(), 1);
    }

    @Test
    void testIsEnded() {
        assertFalse(game.isEnded());
    }

    @Test
    void testAddMoneyPositive() {
        game.addMoney(10);
        assertEquals(game.getMoney(), 110);
    }
    @Test
    void testAddMoneyNegative() {
        game.addMoney(-10);
        assertEquals(game.getMoney(), 90);
    }

    @Test
    void testSetTowers() {
        ArrayList<Tower> testTowers = new ArrayList<Tower>();
        GridPosition gridPos = new GridPosition(3, 4);
        testTowers.add(new Tower(gridPos, game));

        game.setTowers(testTowers);

        assertEquals(gridPos, game.getTowers().get(0).getGridPosition());
    }

    @Test
    void testSetWaveManager() {
        WaveManager testWM = new WaveManager(game);
        testWM.setCurrWave(4);

        game.setWaveManager(testWM);

        assertEquals(4, testWM.getCurrWave());
    }

    @Test
    void testToJson() {
        testPlaceTowerValidLocation();

        JSONObject gameJson = game.toJson();
        JSONArray towersJson = (JSONArray) game.toJson().get("towers");

        assertEquals(1, towersJson.length());
        assertEquals(60, gameJson.getInt("money"));
    }

}
