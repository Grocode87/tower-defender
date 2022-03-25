package model;

import model.direction.Direction;
import model.grid.Grid;
import model.position.GridPosition;
import model.position.Position;
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
    void testConstructor() {
        assertEquals(grid, game.getGrid());
        assertEquals(100, game.getMoney());
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

    @Test
    void testToJsonBullets() {
        game.addBullet(new Bullet(new Position(100, 100),
                new Direction(5, 5), 0, 10));
        game.addBullet(new Bullet(new Position(100, 100),
                new Direction(5, 5), 0, 20));

        JSONObject gameJson = game.toJson();
        JSONArray bulletsJson = gameJson.getJSONArray("bullets");

        assertEquals(2, bulletsJson.length());
    }

    @Test
    void testRemoveTowerExists() {
        game.placeTower(new GridPosition(1, 0));
        game.placeTower(new GridPosition(2, 0));
        assertEquals(2, game.getTowers().size());
        assertEquals(20, game.getMoney());

        game.removeTower(new GridPosition(1, 0));
        assertEquals(1, game.getTowers().size());
        assertEquals(60, game.getMoney());
    }

    @Test
    void testRemoveTowerDoesNotExistAtXandY() {
        game.addMoney(Tower.COST * 3);
        game.placeTower(new GridPosition(1, 0));
        game.placeTower(new GridPosition(2, 0));

        game.removeTower(new GridPosition(3, 3));
        game.removeTower(new GridPosition(3, 0));
        game.removeTower(new GridPosition(1, 3));
        assertEquals(2, game.getTowers().size());
    }

    @Test
    void testUpgradeTowerEnoughMoney() {
        game.placeTower(new GridPosition(1, 0));
        game.placeTower(new GridPosition(2, 0));

        int leftOverMoney = game.getMoney();
        game.addMoney(Tower.UPGRADE_COST);
        game.upgradeTower(new GridPosition(1, 0));

        assertEquals(1, game.getTowers().get(0).getLevel());
        assertEquals(leftOverMoney, game.getMoney());
    }

    @Test
    void testUpgradeTowerNotEnoughMoney() {
        game.placeTower(new GridPosition(1, 0));
        int leftOverMoney = game.getMoney();
        game.upgradeTower(new GridPosition(1, 0));

        assertEquals(0, game.getTowers().get(0).getLevel());
        assertEquals(leftOverMoney, game.getMoney());
    }

    @Test
    void testUpgradeTowerInvalidLocations() {
        game.addMoney(Tower.COST * 3);
        game.placeTower(new GridPosition(1, 0));
        game.placeTower(new GridPosition(2, 0));
        game.placeTower(new GridPosition(3, 0));

        game.upgradeTower(new GridPosition(1, 5));
        game.upgradeTower(new GridPosition(5, 0));
        game.upgradeTower(new GridPosition(5, 5));

        assertEquals(0, game.getTowers().get(0).getLevel());
        assertEquals(0, game.getTowers().get(1).getLevel());
        assertEquals(0, game.getTowers().get(2).getLevel());
    }

    @Test
    void testCheckBulletCollisionNoBullets() {
        assertEquals(0, game.getBullets().size());
        game.checkBulletCollision();
        assertEquals(0, game.getBullets().size());
    }

    @Test
    void testCheckBulletCollisionNoBulletsColliding() {
        for (int i = 0; i < 2 * (WaveManager.SECONDS_BETWEEN_WAVES * TDGame.TICKS_PER_SECOND); i++) {
            game.getWaveManager().tick();
        }

        assertEquals(2, game.getWaveManager().getEnemies().size());

        game.addBullet(new Bullet(new Position(100, 100),
                new Direction(5, 5), 0, 10));
        game.addBullet(new Bullet(new Position(100, 100),
                new Direction(5, 5), 0, 10));

        game.checkBulletCollision();
        assertEquals(2, game.getBullets().size());
    }

    @Test
    void testCheckBulletCollisionOneBulletColliding() {
        for (int i = 0; i < 2 * (WaveManager.SECONDS_BETWEEN_WAVES * TDGame.TICKS_PER_SECOND); i++) {
            game.getWaveManager().tick();
        }

        assertEquals(2, game.getWaveManager().getEnemies().size());

        Position refEnemyPos = game.getWaveManager().getEnemies().get(0).getPosition();
        game.addBullet(new Bullet(new Position(refEnemyPos.getPosX() + Enemy.RADIUS,
                refEnemyPos.getPosY() + Enemy.RADIUS),
                new Direction(5, 5), 0, 10));
        game.addBullet(new Bullet(new Position(100, 100),
                new Direction(5, 5), 0, 10));

        game.checkBulletCollision();
        assertEquals(1, game.getBullets().size());
    }

    @Test
    void testCheckBulletCollisionBulletKillsEnemy() {
        for (int i = 0; i < 2 * (WaveManager.SECONDS_BETWEEN_WAVES * TDGame.TICKS_PER_SECOND); i++) {
            game.getWaveManager().tick();
        }

        assertEquals(2, game.getWaveManager().getEnemies().size());

        Position refEnemyPos = game.getWaveManager().getEnemies().get(0).getPosition();
        game.addBullet(new Bullet(new Position(refEnemyPos.getPosX() + Enemy.RADIUS,
                refEnemyPos.getPosY() + Enemy.RADIUS),
                new Direction(5, 5), 0, 200));

        game.checkBulletCollision();
        assertEquals(1, game.getWaveManager().getEnemies().size());

    }



}
