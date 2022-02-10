package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WaveManagerTest {

    Grid grid = new Grid();
    TDGame game = new TDGame(grid, 100);
    WaveManager waveManager;

    @BeforeEach
    void setup() {
        waveManager = new WaveManager(game);
    }

    @Test
    void testTickMoveEntities() {
        ArrayList<Enemy> testEnemies = new ArrayList<>();
        testEnemies.add(new Enemy(new Position(0,1), "Test", game, 1));

        waveManager.setEnemies(testEnemies);

        waveManager.tick();
        assertEquals(waveManager.getEnemies().get(0).getPosition().getPosX(), 1);
    }
    
    @Test
    void testFirstWaveStart() {
        for (int i = 0; i <= WaveManager.SECONDS_BETWEEN_WAVES * TDGame.TICKS_PER_SECOND; i++) {
            waveManager.stepWave();
        }

        assertEquals(waveManager.getCurrWave(), 1);
        assertEquals(waveManager.getNumEnemiesToSpawn(), WaveManager.BASE_ENEMIES);
    }

    @Test
    void testEnemySpawningPerWave() {
        for (int i = 0; i <= WaveManager.SECONDS_BETWEEN_WAVES * TDGame.TICKS_PER_SECOND; i++) {
            waveManager.stepWave();
        }

        waveManager.stepWave();

        assertEquals(waveManager.getNumEnemiesToSpawn(), WaveManager.BASE_ENEMIES - 1);
        assertEquals(waveManager.getEnemies().size(), 1);

        for (int i = 0; i <= WaveManager.SECONDS_BETWEEN_ENEMIES * TDGame.TICKS_PER_SECOND; i++) {
            waveManager.stepWave();
        }

        assertEquals(waveManager.getNumEnemiesToSpawn(), WaveManager.BASE_ENEMIES - 2);
        assertEquals(waveManager.getEnemies().size(), 2);
    }

    @Test
    void testWaveMessageBase() {
        assertEquals(waveManager.getWaveMessage(), "Place your first tower and get ready!");

    }

    @Test
    void testWaveMessageMiddleOfWave() {
        testEnemySpawningPerWave();

        assertEquals(waveManager.getWaveMessage(), "Wave 1");
    }

    @Test
    void testWaveMessageEndOfWave() {
        testEnemySpawningPerWave();
        waveManager.getEnemies().remove(1);
        waveManager.getEnemies().remove(0);

        assertEquals(waveManager.getWaveMessage(), "Prepare for wave 2");
    }
}