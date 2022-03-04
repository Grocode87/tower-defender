package persistance;


import model.TDGame;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader();
        try {
            TDGame game = reader.read("/file/does/not/exist");
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyGame() {
        JsonReader reader = new JsonReader();
        try {
            TDGame game = reader.read("./data/testEmptyGameStore.json");
            assertEquals(150, game.getMoney());
            assertEquals(0, game.getWaveManager().getEnemies().size());
            assertEquals(0, game.getWaveManager().getCurrWave());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralGame() {
        JsonReader reader = new JsonReader();
        try {
            TDGame game = reader.read("./data/testGeneralGameStore.json");
            assertEquals(90, game.getMoney());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralGameTowers() {
        JsonReader reader = new JsonReader();
        try {
            TDGame game = reader.read("./data/testGeneralGameStore.json");
            assertEquals(3, game.getTowers().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralGameEnemies() {
        JsonReader reader = new JsonReader();
        try {
            TDGame game = reader.read("./data/testGeneralGameStore.json");
            assertEquals(4, game.getWaveManager().getEnemies().size());
            assertEquals(50, game.getWaveManager().getEnemies().get(0).getHealth());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralGameWaveManager() {
        JsonReader reader = new JsonReader();
        try {
            TDGame game = reader.read("./data/testGeneralGameStore.json");
            assertEquals(2, game.getWaveManager().getCurrWave());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}