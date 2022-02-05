package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}
